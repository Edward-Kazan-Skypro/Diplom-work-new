package pro.sky.finalprojectsky.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.finalprojectsky.dto.*;
import pro.sky.finalprojectsky.model.FullAds;
import pro.sky.finalprojectsky.service.AdsService;
import java.util.List;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
public class AdsController {

    private final AdsService adsService;

    public AdsController(AdsService adsService) {
        this.adsService = adsService;
    }

    //Ads block

    //1a new
    @GetMapping(value = "/ads",
            produces = { "application/json" })
    @Operation(summary = "",
            description = "Получить все объявления",
            tags={ "Объявления" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseWrapper.class))) })

    ResponseEntity<ResponseWrapper> getALLAds(){
        return null;
    }

    //2a new
    @PostMapping(value = "/ads",
            produces = { "application/json" },
            consumes = { "multipart/form-data" })
    @Operation(summary = "addImageToAds",
            description = "Добавить объявление",
            tags={ "Объявления" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),

            @ApiResponse(responseCode = "401", description = "Unauthorized"),

            @ApiResponse(responseCode = "403", description = "Forbidden"),

            @ApiResponse(responseCode = "404", description = "Not Found") })

    ResponseEntity<AdsDto> addAd(@RequestParam(value="properties", required=false)  CreateAdsDto properties,
                                 @RequestPart("image") MultipartFile image){
        return null;
    }

    //3a new
    @GetMapping(value = "/ads/{id}",
            produces = { "application/json" })
    @Operation(summary = "Получить информацию об объявлении",
            description = "getFullAd",
            tags={ "Объявления" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FullAds.class))),

            @ApiResponse(responseCode = "404", description = "Not Found") })
    ResponseEntity<FullAds> getAds(@PathVariable("id") Integer id){
        return null;
    }

    //4a new
    @DeleteMapping(value = "/ads/{id}")
    @Operation(summary = "removeAds",
            description = "удалить выбранное объявление",
            tags={ "Объявления" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),

            @ApiResponse(responseCode = "401", description = "Unauthorized"),

            @ApiResponse(responseCode = "403", description = "Forbidden") })
    ResponseEntity<String> removeAds(@PathVariable("id") Integer id){
        if (adsService.removeAds(id)) {
            return ResponseEntity.status(HttpStatus.OK).body("Объявление удалено");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Объявление с таким id отсутствует");
        }
    }


    //5a new
    @PatchMapping(value = "/ads/{id}",
            produces = { "application/json" },
            consumes = { "application/json" })
    @Operation(summary = "Обновить информацию об объявлении",
            description = "updateAds",
            tags={ "Объявления" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdsDto.class))),

            @ApiResponse(responseCode = "401", description = "Unauthorized"),

            @ApiResponse(responseCode = "403", description = "Forbidden"),

            @ApiResponse(responseCode = "404", description = "Not Found") })

    ResponseEntity<AdsDto> updateAds(@PathVariable("id") Integer id, @RequestBody CreateAdsDto body){
        return null;
    }


    //6a new
    @GetMapping(value = "/ads/me",
            produces = { "application/json" })
    @Operation(summary = "Получить объявления авторизованного пользователя",
            description = "getAdsMe",
            tags={ "Объявления" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseWrapper.class))),

            @ApiResponse(responseCode = "401", description = "Unauthorized"),

            @ApiResponse(responseCode = "403", description = "Forbidden"),

            @ApiResponse(responseCode = "404", description = "Not Found") })

    ResponseEntity<ResponseWrapper> getAdsMe(){
        return null;
    }

    //7a new
    @PatchMapping(value = "/ads/{id}/image",
            produces = { "application/octet-stream" },
            consumes = { "multipart/form-data" })
    @Operation(summary = "Обновить картинку объявления",
            description = "updateImage",
            tags={ "Объявления" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/octet-stream", array = @ArraySchema(schema = @Schema(implementation = byte[].class)))),

            @ApiResponse(responseCode = "404", description = "Not Found") })
    ResponseEntity<List<byte[]>> updateImage(@PathVariable("id") Integer id,
                                             @RequestPart("image") MultipartFile image){
        return null;
    }
}

