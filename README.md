# kanjur-backend

Kantin Kejujuran Backend API

## Prerequisite

1. Java 18 ke atas; https://www.oracle.com/java/technologies/downloads/
2. Install Apache Maven; https://maven.apache.org/install.html
3. XAMPP atau MySQL; https://www.apachefriends.org/download.html atau https://dev.mysql.com/downloads/installer/

## Cara Run

1. Bikin db di local namanya '**kanjur**' aja biar sama
2. Setting `application.properties` di `src/main/resources/application.properties` sesuain **username** sama **password** nya
3. Run `/src/main/resources/db/init.sql` di db buat bikin tabel
4. Jalanin command `mvn spring-boot:run`
5. Akses `http://localhost:8080/api/swagger-ui/index.html` buat liat list api yang udah dibuat
