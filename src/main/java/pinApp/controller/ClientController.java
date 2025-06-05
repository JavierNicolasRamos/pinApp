package pinApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pinApp.dto.ClientMetricsDto;
import pinApp.dto.ClientRequestDto;
import pinApp.dto.ClientResponseDto;
import pinApp.service.ClientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @PostMapping
    public ResponseEntity<String> createClient(@RequestBody ClientRequestDto requestDto) {
        this.clientService.createClient(requestDto);
        return ResponseEntity.ok("Client registration successful.");
    }

    @GetMapping("/metrics")
    public ResponseEntity<ClientMetricsDto> getClientMetrics() {
        return ResponseEntity.ok(clientService.getClientMetrics());
    }

    @GetMapping
    public ResponseEntity<List<ClientResponseDto>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClientsWithEstimates());
    }
}
