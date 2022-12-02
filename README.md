# kanjur-backend

Kantin Kejujuran Backend API

## Prerequisite

1. Git; https://git-scm.com/download/win
2. Java 18 ke atas; https://www.oracle.com/java/technologies/downloads/
3. Install Apache Maven; https://maven.apache.org/install.html
4. XAMPP atau MySQL; https://www.apachefriends.org/download.html atau https://dev.mysql.com/downloads/installer/

## Cara Run

1. Clone repository ini
2. Bikin db di local namanya '**kanjur**' aja biar sama
3. Setting `application.properties` di `src/main/resources/application.properties` sesuain **username** sama **password** nya
4. Run `/src/main/resources/db/init.sql` di db buat bikin tabel
5. Jalanin command `mvn spring-boot:run`
6. Akses `http://localhost:8080/api/swagger-ui/index.html` buat liat list api yang udah dibuat
