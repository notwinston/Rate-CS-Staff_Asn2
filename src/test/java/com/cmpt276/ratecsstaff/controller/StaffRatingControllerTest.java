package com.cmpt276.ratecsstaff.controller;

import com.cmpt276.ratecsstaff.model.RoleType;
import com.cmpt276.ratecsstaff.model.StaffRating;
import com.cmpt276.ratecsstaff.service.StaffRatingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(StaffRatingController.class)
class StaffRatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StaffRatingService service;

    private StaffRating sampleRating(Long id) {
        StaffRating r = new StaffRating();
        r.setId(id);
        r.setName("Ada Lovelace");
        r.setEmail("ada@sfu.ca");
        r.setRoleType(RoleType.PROF);
        r.setClarity(8);
        r.setNiceness(9);
        r.setKnowledgeableScore(10);
        return r;
    }

    @Test
    void getIndexReturns200WithRatingsModel() throws Exception {
        when(service.findAll()).thenReturn(List.of(sampleRating(1L)));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("ratings"));
    }

    @Test
    void getDetailReturnsDetailWhenFound() throws Exception {
        when(service.findById(1L)).thenReturn(Optional.of(sampleRating(1L)));

        mockMvc.perform(get("/ratings/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("detail"))
                .andExpect(model().attributeExists("rating"));
    }

    @Test
    void getDetailRedirectsWhenMissing() throws Exception {
        when(service.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/ratings/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void getCreateFormReturnsForm() throws Exception {
        mockMvc.perform(get("/ratings/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attributeExists("rating"));
    }

    @Test
    void postCreateSuccessRedirectsToDetail() throws Exception {
        when(service.isEmailTakenByOther(any(), eq(null))).thenReturn(false);
        when(service.save(any(StaffRating.class))).thenReturn(sampleRating(42L));

        mockMvc.perform(post("/ratings")
                        .param("name", "Ada Lovelace")
                        .param("email", "ada@sfu.ca")
                        .param("roleType", "PROF")
                        .param("clarity", "8")
                        .param("niceness", "9")
                        .param("knowledgeableScore", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ratings/42"));

        verify(service).save(any(StaffRating.class));
    }

    @Test
    void postCreateFailureReturnsFormWithErrors() throws Exception {
        mockMvc.perform(post("/ratings")
                        .param("name", "")
                        .param("email", "not-an-email")
                        .param("roleType", "PROF")
                        .param("clarity", "99")
                        .param("niceness", "9")
                        .param("knowledgeableScore", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attributeHasFieldErrors("rating", "name", "email", "clarity"));

        verify(service, never()).save(any());
    }

    @Test
    void postCreateDuplicateEmailReturnsFormWithEmailError() throws Exception {
        when(service.isEmailTakenByOther(eq("ada@sfu.ca"), eq(null))).thenReturn(true);

        mockMvc.perform(post("/ratings")
                        .param("name", "Ada Lovelace")
                        .param("email", "ada@sfu.ca")
                        .param("roleType", "PROF")
                        .param("clarity", "8")
                        .param("niceness", "9")
                        .param("knowledgeableScore", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attributeHasFieldErrors("rating", "email"));

        verify(service, never()).save(any());
    }

    @Test
    void postDeleteRemovesAndRedirects() throws Exception {
        when(service.existsById(1L)).thenReturn(true);

        mockMvc.perform(post("/ratings/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(service).deleteById(1L);
    }

    @Test
    void postUpdateSuccessRedirectsToDetail() throws Exception {
        when(service.existsById(5L)).thenReturn(true);
        when(service.isEmailTakenByOther(any(), eq(5L))).thenReturn(false);
        when(service.save(any(StaffRating.class))).thenReturn(sampleRating(5L));

        mockMvc.perform(post("/ratings/5")
                        .param("name", "Ada Lovelace")
                        .param("email", "ada@sfu.ca")
                        .param("roleType", "PROF")
                        .param("clarity", "8")
                        .param("niceness", "9")
                        .param("knowledgeableScore", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ratings/5"));

        verify(service).save(any(StaffRating.class));
    }

    @Test
    void postUpdateMissingEntityRedirectsToIndex() throws Exception {
        when(service.existsById(404L)).thenReturn(false);

        mockMvc.perform(post("/ratings/404")
                        .param("name", "Ada Lovelace")
                        .param("email", "ada@sfu.ca")
                        .param("roleType", "PROF")
                        .param("clarity", "8")
                        .param("niceness", "9")
                        .param("knowledgeableScore", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(service, never()).save(any());
    }
}
