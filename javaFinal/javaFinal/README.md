# Envanter Yönetim Sistemi

Bu, Java Final projesi için çeşitli Java programlama kavramlarını gösteren kapsamlı bir Java projesidir.

## Proje Genel Bakış

Envanter Yönetim Sistemi, kullanıcıların ürün ve kategorileri yönetmelerine olanak tanıyan bir Spring Boot uygulamasıdır. İçeriği:

- CRUD işlemleri için RESTful API uç noktaları
- H2 (geliştirme) ve MySQL (üretim) ile veritabanı entegrasyonu
- Çeşitli Java programlama kavramlarının gösterimi

## Kullanılan Teknolojiler

- Java 17
- Spring Boot 3.5.0
- Spring Data JPA
- Hibernate
- Maven
- Docker ve Docker Compose
- Swagger/OpenAPI (API dokümantasyonu için)

## Gösterilen Java Kavramları

1. **Temel Java Konuları**
   - Nesne yönelimli programlama
   - Kalıtım ve polimorfizm
   - İstisna yönetimi

2. **Gelişmiş Java Konuları**
   - Anotasyonlar
   - Yansıma (Spring Framework aracılığıyla)
   - Java Beans

3. **Java 8 Fonksiyonel Programlama**
   - Lambda ifadeleri
   - Streams API
   - Fonksiyonel arayüzler
   - Metot referansları

4. **Kalıtım ve Veri Yapıları**
   - Sınıf kalıtımı
   - Koleksiyon Çerçevesi (Listeler, Haritalar)
   - OOP prensipleri

5. **Sıralama ve Arama Algoritmaları**
   - Birleştirme sıralaması (Merge sort)
   - Hızlı sıralama (Quick sort)
   - İkili arama (Binary search)
   - Kova sıralaması (Bucket sort)

6. **Jenerik Programlama**
   - Jenerik arayüzler
   - Tip parametreleri
   - Sınırlı tip parametreleri

7. **Çok İş Parçacıklı Programlama**
   - İş parçacığı havuzları
   - Eşzamanlı koleksiyonlar
   - CompletableFuture
   - Paralel işleme

8. **Veritabanı Bağlantısı**
   - PostgreSql


9. **J2EE Temelleri**
   - Servletler (Spring MVC aracılığıyla)
   - Bağımlılık Enjeksiyonu
   - Kontrolün Tersine Çevrilmesi

10. **Web Programlama (API)**
    - RESTful API tasarımı
    - HTTP metodları
    - JSON serileştirme/serileştirmekten çözme

11. **Popüler Java Kütüphaneleri ve Çerçeveleri**
    - Spring Boot
    - Spring Data
    - Maven

## Başlarken

### Ön Koşullar

- Java 17 veya üstü
- Maven
- Docker ve Docker Compose (konteynerleştirilmiş dağıtım için)

### Yerel Olarak Çalıştırma

1. Depoyu klonlayın
2. Proje dizinine gidin
3. Uygulamayı çalıştırın:
   ```
   ./mvnw spring-boot:run
   ```

4. Uygulamaya erişin:
   - API: http://localhost:8080/api
   - H2 Konsolu: http://localhost:8080/h2-console
   - Swagger UI: http://localhost:8080/swagger-ui.html

### Docker ile Çalıştırma

1. Docker Compose kullanarak oluşturun ve çalıştırın:
   ```
   docker-compose up -d
   ```

2. Uygulamaya http://localhost:8080 adresinden erişin

## API Uç Noktaları

### Ürünler

- `GET /api/products` - Tüm ürünleri al
- `GET /api/products/{id}` - ID'ye göre ürün al
- `POST /api/products` - Yeni bir ürün oluştur
- `PUT /api/products/{id}` - Bir ürünü güncelle
- `DELETE /api/products/{id}` - Bir ürünü sil
- `GET /api/products/category/{categoryId}` - Kategoriye göre ürünleri al
- `GET /api/products/search?name={name}` - İsme göre ürün ara
- `GET /api/products/price-range?minPrice={min}&maxPrice={max}` - Fiyat aralığına göre ürünleri al
- `GET /api/products/low-stock` - Düşük stoktaki ürünleri al
- `PATCH /api/products/{id}/quantity?quantity={quantity}` - Ürün miktarını güncelle

### Kategoriler

- `GET /api/categories` - Tüm kategorileri al
- `GET /api/categories/{id}` - ID'ye göre kategori al
- `GET /api/categories/name/{name}` - İsme göre kategori al
- `POST /api/categories` - Yeni bir kategori oluştur
- `PUT /api/categories/{id}` - Bir kategoriyi güncelle
- `DELETE /api/categories/{id}` - Bir kategoriyi sil
- `GET /api/categories/search?name={name}` - İsme göre kategori ara

## Dağıtım

Uygulama çeşitli bulut platformlarına dağıtılabilir:

- **Docker**: Sağlanan Dockerfile ve docker-compose.yml dosyalarını kullanın
- **Railway**: Otomatik dağıtım için GitHub deponuzu Railway'e bağlayın
- **AWS**: AWS Elastic Beanstalk veya ECS kullanarak dağıtın

## Lisans

Bu proje MIT Lisansı altında lisanslanmıştır. 