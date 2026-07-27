package za.co.ubuntuhealth.bootstrap.web;

import za.co.ubuntuhealth.bootstrap.config.UbuntuHealthProperties;
import za.co.ubuntuhealth.bootstrap.web.filter.CorrelationIdFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = SystemStatusController.class,
        properties = {
                "ubuntu-health.name=Ubuntu Health",
                "ubuntu-health.environment=test",
                "ubuntu-health.version=test"
        }
)
@EnableConfigurationProperties(UbuntuHealthProperties.class)
@Import(CorrelationIdFilter.class)
class SystemStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void exposesAnUnauthenticatedFoundationStatus() throws Exception {
        mockMvc.perform(get("/api/v1/system/status").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Correlation-ID"))
                .andExpect(jsonPath("$.data.service").value("Ubuntu Health"))
                .andExpect(jsonPath("$.data.status").value("UP"));
    }
}
