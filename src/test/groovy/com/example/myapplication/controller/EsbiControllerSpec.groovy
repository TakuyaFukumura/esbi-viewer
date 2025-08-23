package com.example.myapplication.controller

import com.example.myapplication.entity.EsbiData
import com.example.myapplication.service.EsbiService
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import java.math.BigDecimal
import java.time.LocalDateTime

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

/**
 * EsbiControllerのSpockテスト
 */
class EsbiControllerSpec extends Specification {

    def esbiService = Mock(EsbiService)
    def esbiController = new EsbiController(esbiService)
    def mockMvc = MockMvcBuilders.standaloneSetup(esbiController).build()

    def "ESBIデータ入力ページが正常に表示されること"() {
        given: "空のESBIデータ"
        esbiService.getLatestDataByUser("default_user") >> Optional.empty()

        when: "入力ページにアクセス"
        def result = mockMvc.perform(get("/esbi/input"))

        then: "ステータスが200でinputビューが返される"
        result.andExpect(status().isOk())
              .andExpect(view().name("esbi/input"))
              .andExpect(model().attributeExists("esbiData"))
    }

    def "ESBIデータ保存が正常に動作すること"() {
        given: "保存するESBIデータ"
        def employeeIncome = new BigDecimal("5000000")
        def selfEmployedIncome = new BigDecimal("2000000")
        def businessOwnerIncome = new BigDecimal("1000000")
        def investorIncome = new BigDecimal("500000")
        
        def savedData = new EsbiData()
        savedData.id = 1L
        savedData.userName = "default_user"
        savedData.employeeIncome = employeeIncome
        savedData.selfEmployedIncome = selfEmployedIncome
        savedData.businessOwnerIncome = businessOwnerIncome
        savedData.investorIncome = investorIncome
        
        esbiService.saveEsbiData("default_user", employeeIncome, selfEmployedIncome, businessOwnerIncome, investorIncome) >> savedData

        when: "データを保存"
        def result = mockMvc.perform(post("/esbi/save")
                .param("employeeIncome", "5000000")
                .param("selfEmployedIncome", "2000000")
                .param("businessOwnerIncome", "1000000")
                .param("investorIncome", "500000"))

        then: "可視化ページにリダイレクト"
        result.andExpect(status().is3xxRedirection())
              .andExpect(redirectedUrl("/esbi/visualization"))
        
        and: "サービスが正しく呼ばれる"
        1 * esbiService.saveEsbiData("default_user", employeeIncome, selfEmployedIncome, businessOwnerIncome, investorIncome)
    }

    def "可視化ページでデータが正常に表示されること"() {
        given: "保存されたESBIデータ"
        def esbiData = new EsbiData()
        esbiData.id = 1L
        esbiData.userName = "default_user"
        esbiData.employeeIncome = new BigDecimal("5000000")
        esbiData.selfEmployedIncome = new BigDecimal("2000000")
        esbiData.businessOwnerIncome = new BigDecimal("1000000")
        esbiData.investorIncome = new BigDecimal("500000")
        esbiData.createdAt = LocalDateTime.now()
        
        def totalIncome = new BigDecimal("8500000")
        
        esbiService.getLatestDataByUser("default_user") >> Optional.of(esbiData)
        esbiService.getTotalIncome(esbiData) >> totalIncome
        esbiService.getPercentage(esbiData.employeeIncome, totalIncome) >> 58.82
        esbiService.getPercentage(esbiData.selfEmployedIncome, totalIncome) >> 23.53
        esbiService.getPercentage(esbiData.businessOwnerIncome, totalIncome) >> 11.76
        esbiService.getPercentage(esbiData.investorIncome, totalIncome) >> 5.88

        when: "可視化ページにアクセス"
        def result = mockMvc.perform(get("/esbi/visualization"))

        then: "ステータスが200でvisualizationビューが返される"
        result.andExpect(status().isOk())
              .andExpect(view().name("esbi/visualization"))
              .andExpect(model().attribute("esbiData", esbiData))
              .andExpect(model().attribute("totalIncome", totalIncome))
              .andExpect(model().attributeExists("employeePercentage"))
              .andExpect(model().attributeExists("selfEmployedPercentage"))
              .andExpect(model().attributeExists("businessOwnerPercentage"))
              .andExpect(model().attributeExists("investorPercentage"))
    }

    def "データがない場合の可視化ページが正常に表示されること"() {
        given: "データが存在しない"
        esbiService.getLatestDataByUser("default_user") >> Optional.empty()

        when: "可視化ページにアクセス"
        def result = mockMvc.perform(get("/esbi/visualization"))

        then: "ステータスが200でno dataフラグが設定される"
        result.andExpect(status().isOk())
              .andExpect(view().name("esbi/visualization"))
              .andExpect(model().attribute("noData", true))
    }

    def "ガイドページが正常に表示されること"() {
        when: "ガイドページにアクセス"
        def result = mockMvc.perform(get("/esbi/guide"))

        then: "ステータスが200でguideビューが返される"
        result.andExpect(status().isOk())
              .andExpect(view().name("esbi/guide"))
    }
}
