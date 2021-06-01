package com.kotlin.ktdemo.Controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.json.simple.JSONArray
import org.springframework.http.*
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponents
import org.springframework.web.util.UriComponentsBuilder
import java.util.*
import kotlin.collections.HashMap

@RestController
class RestAPI {

    @GetMapping("/getKobisData")
    private fun callAPI() : String {

        val result  = HashMap<String, Any>()
        var jsonInString = ""

        try {
            val factory = HttpComponentsClientHttpRequestFactory();

            factory.setConnectTimeout(5000)
            factory.setReadTimeout(5000)

            val restTemplate = RestTemplate(factory)
            //restTemplate은 Rest방식 api를 호출할 수 있는 spring 내장 클래스이다.
            //json, xml 응답을 모두 받을 수 있다.

            //header 클래스를 정의해 주고, url을 정의해 주고 exchange method로 api를 호출한다.
            val header = HttpHeaders()
            val entity = HttpEntity<Map<String, Any>>(header)
            val url = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json"
            val uri : UriComponents
                    = UriComponentsBuilder.fromHttpUrl(url + "?" + "key=827c3342e912bdb8cdbeb7cf0625b764&targetDt=20210430").build()

            //API를 호출해 MAP타입으로 전달 받는다.
            val resultMap : ResponseEntity<Map<*, *>> = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, Map::class.java)

            result.put("statusCode", resultMap.getStatusCodeValue()); //http status code를 확인
            result.put("header", resultMap.getHeaders()); //헤더 정보 확인
            resultMap.body?.let { result.put("body", it) }; //실제 데이터 정보 확인

            //데이터를 제대로 전달 받았는지 확인 string형태로 파싱해줌

            val mapper  = ObjectMapper()
            jsonInString = mapper.writeValueAsString(resultMap.getBody());

        } catch (e: Exception){
            when(e) {
                is HttpClientErrorException, is HttpServerErrorException -> {
                    result.put("statusCode", "e.getStatusCode()");
                    result.put("body", "e.getStatusText()");
                    System.out.println("error!");
                    System.out.println(e.toString());
                }else -> {
                result.put("statusCode", "999");
                result.put("body", "excpetion오류");
                System.out.println(e.toString());
            }
            }
        }

        return jsonInString;
    }
}