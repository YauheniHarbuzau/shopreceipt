# Shop Receipt

**Консольное приложение, реализующее формирование кассового чека.**</br>
**Сформированный кассовый чек выводится в консоль и в файл.**

**Автор Евгений Гарбузов**

***

### Описание принципа работы

Приложение принимает данные через консоль в виде строки (*String*) с набором параметров формата:

`ID товара - количество товара`

Пример:

`1-3 2-4 3-5 card-1111`

*1-3* - товар с *ID 1* в количестве *3* шт.,</br>
*2-4* - товар с *ID 2* в количестве *4* шт.,</br>
*3-5* - товар с *ID 3* в количестве *5* шт.,</br>
*card-1111* - платежная (скидочная) карта с номеров *1111*.

- Параметры следует разделять одним пробелом.
- Параметров, содержащих *ID* товара и его количество, может быть сколько угодно много.
- Параметр, содержащий номер платежной карты, необязательный и может быть только один.
- Параметры, содержащие *ID* товара или номер платежной карты, отсутствующие в базе данных, не учитываются</br>
  (игнорируются при формировании чека).
- При наличии от пяти акционных товаров, на них будет дана скидки *10%*.

***

### Примеры работы приложения

Пример 1 (вывод кассового чека в консоль)

```
Make an input:
2-1 1-2 5-1 10-3 card-1234
```

```
|****************RECEIPT****************|
|***************************************|
| bread        |  2,00$ |   x2 |  4,00$ |
| long loaf    |  2,50$ |   x1 |  2,50$ |
| chicken      |  4,50$ |   x1 |  4,50$ |
| water        |  1,00$ |   x3 |  3,00$ |
|***************************************|
| full price:                  | 14,00$ |
| discount:                    |  5,00% |
| total price:                 | 13,30$ |
|***************************************|
```

Пример 2 (вывод кассового чека в консоль)

```
Make an input:
3-1 7-1 11-2 4-2 20-1 card-4321
```

```
|****************RECEIPT****************|
|***************************************|
| flour        |  1,50$ |   x1 |  1,50$ |
| eggs         |  2,50$ |   x2 |  5,00$ |
| beef         |  7,50$ |   x1 |  7,50$ |
| soda         |  1,50$ |   x2 |  3,00$ |
|***************************************|
| full price:                  | 17,00$ |
| discount:                    |  0,00% |
| total price:                 | 17,00$ |
|***************************************|
```

Во втором примере в данных пять товаров, а на сформированном чеке четыре.</br>
Это связано с тем, что товар с *ID* 20 не учитывается, так как отсутствует в базе данных.</br>
Так же в чеке нет скидки, так как платежная карта под номером *4321* отсутствует в базе данных.

Пример формирования кассового чека в формате *pdf*:
[receipt_example.pdf](resources/pdfexample/receipt_example.pdf "receipt_example.pdf").

***

### Параметры приложения

- Java 17
- Система автоматической сборки Gradle 7.5.1
- База данных PostgreSQL*
- Точка входа в приложение - класс
  [ShopreceiptApplication](src/main/java/com/example/shopreceipt/ShopreceiptApplication.java "ShopreceiptApplication.java")
- Реализованы REST-контроллеры для поиска всех товаров и платежных карт, поиска по *ID*, сохранения, удаления по *ID*
- Реализованы тесты

*Информация для подключения к базе данных (*url*, *username*, *password*) находится в
[application.yml](src/main/resources/application.yml "application.yml").</br>
Автозаполнение базы данных происходит в классе
[InsertToDataBase](src/main/java/com/example/shopreceipt/util/InsertToDataBase.java "InsertToDataBase.java").

***

### Библиотеки и зависимости, используемые в приложении

- *Spring Boot Web*, *Data JPA*, *Validation*, *AOP*
- *PostgreSQL JDBC Driver* для работы с базой данных
- *Project Lombok* для автоматической генерации геттеров, сеттеров, конструкторов
- *JDOM2*, *IText 7* для генерации *xml* и *pdf* файлов

Точную информацию о библиотеках и зависимостях можно посмотреть в
[build.gradle](build.gradle "build.gradle").

***