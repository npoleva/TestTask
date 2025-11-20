# Сервис по поиску N-го минимального числа в списке

## Алгоритм

Используется алгоритм **QuickSelect**:
- Случайным образом выбирается опорный элемент (pivot)
- Массив делится на 2 массива: ***с элементами меньше pivot*** и ***больше либо равно pivot***
- Рекурсивно ищем N-й минимум только в нужной части массива

**Сложность:**
- В среднем `O(n)`
- В худшем случае `O(n²)` (Чтобы избежать худшего случая, используется класс Random для определения опорного элемента)

## Пример

### Excel файл (`numbers.xlsx`):
| Числа |
|-------|
| 5     |
| 2     |
| 9     |
| 1     |
| 7     |

### JSON-запрос:
```json
{
  "filePath": "/Users/.../numbers.xlsx",
  "n": 3
}
```

### JSON-ответ:
```json
{
  "minN": 5
}
```
***(3-й минимум в массиве [5,2,9,1,7] → 5)***


## Инструкция по сборке и запуску

**Требования:**
- Java 17 или выше
- Maven 3.6 или выше

**1. Клонирование репозитория:**
```bash
git clone https://github.com/npoleva/TestTask
cd TestTask
```
**2. Проверка окружения:**
```bash
java -version
mvn -version
```

**3. Сборка проекта:**
```bash
mvn clean package
```

**4. Запуск приложения:**
```bash
java -jar target/nth-smallest-service-1.0.0.jar
```

**5. Проверка работы:**
- Приложение будет доступно по адресу: http://localhost:8080

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

Пример тестового запроса через curl:
```bash
curl -X POST "http://localhost:8080/" \
     -H "Content-Type: application/json" \
     -d '{"filePath":"/path/to/numbers.xlsx","n":3}'
```

