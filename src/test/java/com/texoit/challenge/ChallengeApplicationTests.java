package com.texoit.challenge;

import static org.hamcrest.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.texoit.challenge.model.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ChallengeApplicationTests {

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private MockMvc mockMvc;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private ObjectMapper mapper;

  @Test
  public void assertThatAllMoviesAreReturnedAndOneOfTheirFieldsTypesIsAsRequired() throws Exception {
    this.mockMvc.perform(get("/movies"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$").isNotEmpty())
        .andExpect(jsonPath("$[0].id").isNumber())
        .andExpect(jsonPath("$[0].title").isString())
        .andExpect(jsonPath("$[0].year").isNumber())
        .andExpect(jsonPath("$[0].producers").isString())
        .andExpect(jsonPath("$[0].studios").isString())
        .andExpect(jsonPath("$[0].winner").isString());
  }

  @Test
  public void assertMovieWithIdOneIsReturnedAndItsFieldsTypesAreAsRequired() throws Exception {
    this.mockMvc.perform(get("/movies/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.title").isString())
        .andExpect(jsonPath("$.year").isNumber())
        .andExpect(jsonPath("$.producers").isString())
        .andExpect(jsonPath("$.studios").isString())
        .andExpect(jsonPath("$.winner").isString());
  }

  @Test
  public void assertMovieIsInsertedAndTheReturnedFieldsTypesAreAsRequired() throws Exception {
    this.mockMvc.perform(post("/movies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(new Movie(2022, "Avatar", "Bilibili", "John Snow", "yes"))))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(any(Integer.class)))
        .andExpect(jsonPath("$.title").value("Avatar"))
        .andExpect(jsonPath("$.year").value(2022))
        .andExpect(jsonPath("$.studios").value("Bilibili"))
        .andExpect(jsonPath("$.producers").value("John Snow"))
        .andExpect(jsonPath("$.winner").value("yes"));
  }

  @Test
  public void assertMovieIsUpdatedAndTheReturnedFieldsTypesAreAsRequired() throws Exception {
    this.mockMvc.perform(put("/movies/2")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(new Movie(2023, "OpenHeimer", "German", "John Snow", null))))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(2))
        .andExpect(jsonPath("$.title").value("OpenHeimer"))
        .andExpect(jsonPath("$.year").value(2023))
        .andExpect(jsonPath("$.producers").value("John Snow"))
        .andExpect(jsonPath("$.studios").value("German"))
        .andExpect(jsonPath("$.winner").isEmpty());
  }

  @Test
  public void assertMovieIsDeletedAndTheReturnIsCorrect() throws Exception {
    this.mockMvc.perform(delete("/movies/3"))
        .andExpect(status().isNoContent());
  }

  @Test
  public void assertACaseOfWinnersIntervalForMinAndMaxAreAsExpectedAndFieldsTypesCorrectlyReturned() throws Exception {
    this.mockMvc.perform(get("/movies/winners-intervals")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.min").isArray())
        .andExpect(jsonPath("$.min").isNotEmpty())
        .andExpect(jsonPath("$.min.[0].producer").value("Joel Silver"))
        .andExpect(jsonPath("$.min.[0].interval").value(1))
        .andExpect(jsonPath("$.min.[0].previousWin").value(1990))
        .andExpect(jsonPath("$.min.[0].followingWin").value(1991))
        .andExpect(jsonPath("$.max").isArray())
        .andExpect(jsonPath("$.max").isNotEmpty())
        .andExpect(jsonPath("$.max.[0].producer").value("Matthew Vaughn"))
        .andExpect(jsonPath("$.max.[0].interval").value(13))
        .andExpect(jsonPath("$.max.[0].previousWin").value(2002))
        .andExpect(jsonPath("$.max.[0].followingWin").value(2015));
  }

}
