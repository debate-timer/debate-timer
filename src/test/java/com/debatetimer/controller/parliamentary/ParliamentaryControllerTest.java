package com.debatetimer.controller.parliamentary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.debatetimer.BaseControllerTest;
import com.debatetimer.domain.member.Member;
import com.debatetimer.domain.parliamentary.ParliamentaryTable;
import com.debatetimer.dto.parliamentary.request.ParliamentaryTableCreateRequest;
import com.debatetimer.dto.parliamentary.request.TableInfoCreateRequest;
import com.debatetimer.dto.parliamentary.request.TimeBoxCreateRequests;
import com.debatetimer.dto.parliamentary.response.ParliamentaryTableResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ParliamentaryControllerTest extends BaseControllerTest {

    @Nested
    class save {

        @Test
        void 토론_테이블을_생성한다() {
            Member bito = fixtureGenerator.generateMember("비토");
            ParliamentaryTableCreateRequest bitoTableRequest = dtoGenerator.generateParliamentaryTableCreateRequest("비토 테이블");
            TableInfoCreateRequest requestTableInfo = bitoTableRequest.info();
            TimeBoxCreateRequests requestTimeBoxes = bitoTableRequest.table();

            ParliamentaryTableResponse response = RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .queryParam("memberId", bito.getId())
                    .body(bitoTableRequest)
                    .when().post("/api/table/parliamentary")
                    .then().log().all()
                    .statusCode(201)
                    .extract().as(ParliamentaryTableResponse.class);

            assertAll(
                    () -> assertThat(response.info().name()).isEqualTo(requestTableInfo.name()),
                    () -> assertThat(response.table().timeBoxes()).hasSize(requestTimeBoxes.timeBoxCreateRequests().size())
            );
        }
    }

    @Nested
    class getTable {

        @Test
        void 의회식_테이블을_조회한다() {
            Member bito = fixtureGenerator.generateMember("비토");
            ParliamentaryTable bitoTable = fixtureGenerator.generateParliamentaryTable(bito);
            fixtureGenerator.generateParliamentaryTimeBox(bitoTable, 1);
            fixtureGenerator.generateParliamentaryTimeBox(bitoTable, 2);

            ParliamentaryTableResponse response = RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .queryParam("memberId", bito.getId())
                    .when().get("/api/table/parliamentary/" + bitoTable.getId())
                    .then().log().all()
                    .statusCode(200)
                    .extract().as(ParliamentaryTableResponse.class);

            assertAll(
                    () -> assertThat(response.id()).isEqualTo(bitoTable.getId()),
                    () -> assertThat(response.table().timeBoxes()).hasSize(2)
            );
        }
    }

    @Nested
    class updateTable {

        @Test
        void 의회식_토론_테이블을_업데이트한다() {
            Member bito = fixtureGenerator.generateMember("커찬");
            ParliamentaryTable bitoTable = fixtureGenerator.generateParliamentaryTable(bito);
            ParliamentaryTableCreateRequest renewTableRequest =  dtoGenerator.generateParliamentaryTableCreateRequest("새로운 테이블");
            TableInfoCreateRequest renewTableInfo = renewTableRequest.info();
            TimeBoxCreateRequests renewTimeBoxes = renewTableRequest.table();

            ParliamentaryTableResponse response = RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .queryParam("memberId", bito.getId())
                    .body(renewTableRequest)
                    .when().put("/api/table/parliamentary/" + bitoTable.getId())
                    .then().log().all()
                    .statusCode(200)
                    .extract().as(ParliamentaryTableResponse.class);

            assertAll(
                    () -> assertThat(response.id()).isEqualTo(bitoTable.getId()),
                    () -> assertThat(response.info().name()).isEqualTo(renewTableInfo.name()),
                    () -> assertThat(response.table().timeBoxes()).hasSize(renewTimeBoxes.timeBoxCreateRequests().size())
            );
        }
    }

    @Nested
    class deleteTable {

        @Test
        void 의회식_토론_테이블을_삭제한다() {
            Member bito = fixtureGenerator.generateMember("비토");
            ParliamentaryTable bitoTable = fixtureGenerator.generateParliamentaryTable(bito);
            fixtureGenerator.generateParliamentaryTimeBox(bitoTable, 1);
            fixtureGenerator.generateParliamentaryTimeBox(bitoTable, 2);

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .queryParam("memberId", bito.getId())
                    .when().delete("/api/table/parliamentary/" + bitoTable.getId())
                    .then().log().all()
                    .statusCode(204);
        }
    }
}