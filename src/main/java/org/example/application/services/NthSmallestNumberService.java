package org.example.application.services;

import lombok.extern.slf4j.Slf4j;
import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.example.application.dto.RequestDto;
import org.example.application.dto.ResponseDto;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static java.util.Collections.swap;

@Slf4j
@Service
public class NthSmallestNumberService {
    private int quickSelect(List<Integer> nums, int k) {
        return quickSelectRecursive(nums, 0, nums.size() - 1, k);
    }

    private int quickSelectRecursive(List<Integer> nums, int left, int right, int k) {
        if (left == right) {
            return nums.get(left);
        }

        int pivotIndex = partition(nums, left, right);

        int countInLeft = pivotIndex - left + 1;

        if (k == countInLeft) {
            return nums.get(pivotIndex);
        } else if (k < countInLeft) {
            return quickSelectRecursive(nums, left, pivotIndex - 1, k);
        } else {
            return quickSelectRecursive(nums, pivotIndex + 1, right, k - countInLeft);
        }
    }

    private int partition(List<Integer> nums, int left, int right) {
        int pivotIndex = new Random().nextInt(right - left + 1) + left;
        int pivot = nums.get(pivotIndex);

        swap(nums, pivotIndex, right);

        int smallerThanPivotElementsRightBoundary = left;

        for (int j = left; j < right; j++) {
            if (nums.get(j) < pivot) {
                if (j == smallerThanPivotElementsRightBoundary) {
                    smallerThanPivotElementsRightBoundary++;
                    continue;
                }
                int tmp = nums.get(smallerThanPivotElementsRightBoundary);
                nums.set(smallerThanPivotElementsRightBoundary, nums.get(j));
                nums.set(j, tmp);
                smallerThanPivotElementsRightBoundary++;
            }
        }

        int tmp = nums.get(smallerThanPivotElementsRightBoundary);
        nums.set(smallerThanPivotElementsRightBoundary, nums.get(right));
        nums.set(right, tmp);

        return smallerThanPivotElementsRightBoundary;
    }

    public ResponseDto findNthSmallestNumber(RequestDto requestDto) throws  IOException {
        ArrayList<Integer> numbers = getNumbersFromFile(requestDto.filePath);

        if (numbers.isEmpty()) {
            log.error("Файл пустой или не содержит чисел!");
            throw new IllegalArgumentException("Файл пустой или не содержит чисел!");
        }
        if (requestDto.n > numbers.size()) {
            log.error("Номер минимального числа не может быть больше размера массива!");
            throw new IllegalArgumentException("Номер минимального числа не может быть больше размера массива!");
        }
        else if (requestDto.n < 1) {
            log.error("Номер минимального числа не может быть меньше 1!");
            throw new IllegalArgumentException("Номер минимального числа не может быть меньше 1!");
        }

        var responseDto = new ResponseDto();
        responseDto.minN = quickSelect(numbers, requestDto.n);

        return responseDto;
    }

    private ArrayList<Integer> getNumbersFromFile(String filePath) throws IOException {
        ArrayList<Integer> data = new ArrayList<>();

        if (filePath == null || filePath.isEmpty()) {
            log.error("Путь к файлу не может быть null или пустым!");
            throw new IllegalArgumentException("Путь к файлу не может быть null или пустым!");
        }

        try (FileInputStream file = new FileInputStream(filePath);
             ReadableWorkbook wb = new ReadableWorkbook(file)) {

            Sheet sheet = wb.getFirstSheet();

            try (Stream<Row> rows = sheet.openStream()) {
                rows.forEach(r -> {
                    Cell firstCell = r.iterator().hasNext() ? r.iterator().next() : null;
                    if (firstCell != null) {
                        String value = firstCell.getRawValue();
                        if (value != null && !value.isEmpty()) {
                            try {
                                data.add(Integer.parseInt(value));
                            } catch (NumberFormatException e) {
                                log.error(e.getMessage(), e);
                                throw new IllegalArgumentException("Ячейка содержит символ неверного формата: " + value, e);
                            }

                        }
                    }
                });
            }
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }

        return data;
    }

}
