/*
 * Copyright 2014 Michael J. Simons.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bofa.sstradingreport.managementreport;

import com.bofa.sstradingreport.support.ExceptionHandlerAdvice;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.joor.Reflect;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static java.time.LocalDate.now;

/**
 * @author Michael J. Simons, 2014-02-20
 */
public class TradeReportControllerTest {

    @Rule
    public final RestDocumentation restDocumentation = new RestDocumentation("target/generated-snippets");

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldgetTradeReports() throws Exception {
	final List<TradeReportEntity> allbikes = Arrays.asList(Reflect.on(TradeReportEntity.class).create()
		    .set("id", 4711)
		    .set("name", "Bike 1")
		    .set("tradeDate", GregorianCalendar.from(LocalDate.of(2015, Month.JANUARY, 1).atStartOfDay(ZoneId.systemDefault())))
		    .call("getTradeReport")
		    .get(),
		Reflect.on(TradeReportEntity.class).create()
		    .set("id", 23)
		    .set("tradeDate", GregorianCalendar.from(LocalDate.of(2014, Month.JANUARY, 1).atStartOfDay(ZoneId.systemDefault())))
		    .call("getTradeReport")
		    .get()
	);
	final List<TradeReportEntity> activeBikes = Arrays.asList(allbikes.get(0));

	final TradeReportRepository repository = mock(TradeReportRepository.class);
	stub(repository.findAll(any(Sort.class))).toReturn(allbikes);
//	stub(repository.findByDecaommissionedOnIsNull(any(Sort.class))).toReturn(activeBikes);

	final TradeReportController controller = new TradeReportController(repository);

	final MockMvc mockMvc = MockMvcBuilders
		.standaloneSetup(controller)
		.setControllerAdvice(new ExceptionHandlerAdvice())
		.apply(documentationConfiguration(this.restDocumentation))		
		.build();

	mockMvc
		.perform(
			get("http://localhost:8088/sstradingreport/api/bikes")
			.param("all", "true")
		)
		.andExpect(status().isOk())
		.andExpect(content().string(objectMapper.writeValueAsString(allbikes)))
		.andDo(document("api/bikes/get",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestParameters(
				    parameterWithName("all").description("Flag, if all bikes, including decommissioned bikes, should be returned.")
				),
				responseFields(
					fieldWithPath("[]").description("An array of bikes"),
					fieldWithPath("[].id").description("The unique Id of the bike"),
					fieldWithPath("[].name").description("The name of the bike"),
					fieldWithPath("[].tradeDate").description("The date the bike was bought")
				)
			)
		)
		;

	mockMvc
		.perform(
			get("http://biking.michael-simons.eu/api/bikes")
		)
		.andExpect(status().isOk())
		.andExpect(content().string(objectMapper.writeValueAsString(activeBikes)))
		;

	Mockito.verify(repository).findAll(Mockito.any(Sort.class));
//	Mockito.verify(repository).findByDecommissionedOnIsNull(Mockito.any(Sort.class));
	verifyNoMoreInteractions(repository);
    }

}
