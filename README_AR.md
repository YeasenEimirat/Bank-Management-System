# Bank Management System - Java Console OOP

تم تحويل مشروع C++ Bank Management System إلى مشروع Java Console كامل بنفس فكرة OOP والملفات النصية.

## المميزات الموجودة

- Login System مع 3 محاولات.
- تشفير وفك تشفير كلمة المرور مثل مشروع C++ باستخدام key = 2.
- صلاحيات Users باستخدام Bitwise Permissions.
- Client Management: عرض، إضافة، حذف، تعديل، بحث.
- Transactions: Deposit, Withdraw, Total Balances, Transfer.
- Transfer Log.
- Login Register.
- Currency Exchange: عرض العملات، البحث بالكود أو الدولة، تعديل السعر، Currency Calculator.
- تخزين البيانات في ملفات TXT داخل مجلد `data`.

## طريقة التشغيل على Windows

شغل الملف:

```bat
run.bat
```

أو يدويًا:

```bat
javac -encoding UTF-8 -d out src\main\java\com\bankmanagement\*.java
java -cp out com.bankmanagement.Main
```

## طريقة التشغيل على Linux / Mac

```bash
./run.sh
```

أو يدويًا:

```bash
javac -encoding UTF-8 -d out src/main/java/com/bankmanagement/*.java
java -cp out com.bankmanagement.Main
```

## التشغيل باستخدام Maven

```bash
mvn compile
java -cp target/classes com.bankmanagement.Main
```

## ملفات البيانات

لا تحذف مجلد `data` لأنه يحتوي على:

- `BClients.txt`
- `BUsers.txt`
- `Currencies.txt`
- `LoginRegister.txt`
- `TransferLog.txt`

## حسابات تجربة

حسب البيانات الموجودة في الملف المحول:

- Username: `User1` / Password: `9898`
- Username: `User2` / Password: `1234`
- Username: `w` / Password: `w`

ملاحظة: كلمات المرور داخل `BUsers.txt` تكون مخزنة بشكل مشفر مثل مشروع C++ الأصلي.

## ملاحظات مهمة لتسجيل الدخول

بعد التعديل، عند نجاح تسجيل الدخول ينتقل البرنامج مباشرة إلى Main Menu بدون شاشة توقف.
إذا شغّلت المشروع من IntelliJ/Eclipse/NetBeans، سيحاول البرنامج العثور على مجلد `data` تلقائيًا حتى لا يتم إنشاء ملف مستخدمين فارغ في مسار تشغيل مختلف.

حسابات الدخول الأساسية:

- Username: `User1` / Password: `9898`
- Username: `User2` / Password: `1234`
- Username: `w` / Password: `w`

مهم: القيم الموجودة داخل `BUsers.txt` مشفّرة، لذلك لا تدخل النص الظاهر في الملف ككلمة مرور دائمًا.

## JavaFX GUI Version
تمت إضافة واجهة رسومية JavaFX للمشروع بدون حذف نسخة الـ Console الأصلية.

### تشغيل نسخة JavaFX
1. افتح المشروع داخل IntelliJ IDEA أو Eclipse كـ Maven Project.
2. تأكد أن Java 17 أو أحدث مثبت على الجهاز.
3. شغل الكلاس التالي:
   `com.bankmanagement.fx.BankFxApp`

أو من خلال Maven:
```bash
mvn clean javafx:run
```

### الواجهات المضافة
- Login Screen.
- Dashboard.
- Clients Management: عرض، إضافة، تعديل، حذف، بحث.
- Transactions: Deposit, Withdraw, Transfer.
- Transfer Log.
- Users Management مع Permissions.
- Login Register.
- Currency Exchange مع تحديث سعر العملة والتحويل بين العملات.

### ملاحظة مهمة
نسخة JavaFX تستخدم نفس ملفات البيانات الموجودة داخل مجلد `data`، لذلك أي تعديل من الواجهة يتم حفظه في ملفات TXT مثل النسخة الأصلية.
