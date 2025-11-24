package contracts.audit

org.springframework.cloud.contract.spec.Contract.make {
    description("should return audit record for patient")
    request {
        method GET()
        url("/api/v1/audit/Patient/100") {
            queryParameters {
                parameter("page", value("0"))
                parameter("size", value("20"))
            }
        }
    }
    response {
        status OK()
        headers {
            contentType(applicationJson())
        }
        body(
            """
            {
              "content": [
                {
                  "entityType": "Patient",
                  "entityId": 100,
                  "action": "CREATED",
                  "username": "contract-user"
                }
              ]
            }
            """
        )
        bodyMatchers {
            jsonPath('$.content[0].entityType', byEquality())
            jsonPath('$.content[0].entityId', byEquality())
            jsonPath('$.content[0].action', byEquality())
            jsonPath('$.content[0].username', byEquality())
        }
    }
}









