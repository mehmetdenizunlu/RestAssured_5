import GoRest.User;
import Model.Location;
import Model.Place;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;


public class PathAndJsonPath {
    @Test
    public void extractingPath() {

        String postCode =
                given()


                        .when()
                        .get("http://api.zippopotam.us/us/90210")


                        .then()
                        .log().body()
                        .extract().path("'post code'");
        System.out.println("postCode = " + postCode);

    }


    @Test
    public void extractingJsonPath() {

        int postCode =
                given()


                        .when()
                        .get("http://api.zippopotam.us/us/90210")


                        .then()
                        .log().body()
                        .extract().jsonPath().getInt("'post code'")
                //tip dönüşümü otomatik , uygun tip verilmelidir

                ;
        System.out.println("postCode = " + postCode);

    }

    @Test
    public void getUsers() {

        Response response =
                given()

                        .when()
                        .get("https://gorest.co.in/public/v2/users")

                        .then()
                        .log().body()
                        .extract().response();

        int idPath = response.path("[2].id");
        int idJsonPath = response.jsonPath().getInt("[2].id");

        System.out.println("idJsonPath = " + idJsonPath);
        System.out.println("idPath = " + idPath);


        // tamamını alabilmek için

        User[] users = response.as(User[].class); // as nesne dönüşümünde (POJO) dizi destekli
        List<User> userListPath = response.jsonPath().getList("", User.class); // JsonPath ise List olarak veriyor

        System.out.println("users = " + Arrays.toString(users));
        System.out.println("userListPath = " + userListPath);


    }

    @Test
    public void getUsersV1() {
        Response body =
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                       .log().body()
                        .extract().response();


        List<User> dataUsers = body.jsonPath().getList("data", User.class);
        //JSONPATH bir response içindeki bir parçayı nesneye dönüştürebiliriz
        System.out.println("dataUsers = " + dataUsers);

    }

    // Daha önceki örneklerde (as) Clas dönüşümleri için tüm yapıya karşılık gelen
    // gereken tüm classları yazarak dönüştürüp istediğimiz elemanlara ulaşıyorduk.

    // Burada ise(JsonPath) aradaki bir veriyi clasa dönüştürerek bir list olarak almamıza
    // imkan veren JSONPATH i kullandık.Böylece tek class ile veri alınmış oldu
    // diğer class lara gerek kalmadan

    // path : class veya tip dönüşümüne imkan veremeyen direk veriyi verir. List<String> gibi
    // jsonPath : class dönüşümüne ve tip dönüşümüne izin vererek , veriyi istediğimiz formatta verir.



    @Test
    public void getZipCode(){
        Response response=
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .extract().response()
                ;

        Location locPathAs=response.as(Location.class); //bütün classları yazmak zorundasın
        System.out.println("locPathAs.getPlaces() = " + locPathAs.getPlaces());

        List<Place> places=response.jsonPath().getList("places",Place.class); // nokta atısı istediğimiz nesneyi aldık
        System.out.println("places = " + places);



    }


}
