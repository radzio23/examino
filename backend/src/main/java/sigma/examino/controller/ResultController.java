package sigma.examino.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sigma.examino.dto.ExamResultDTO;
import sigma.examino.model.Result;
import sigma.examino.model.User;
import sigma.examino.repository.ResultRepository;
import sigma.examino.repository.UserRepository;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/results")
public class ResultController {

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Pobiera listę wyników egzaminów dla zalogowanego użytkownika.
     */
    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ExamResultDTO>> getMyResults(Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Użytkownik nie istnieje"));

        List<Result> results = resultRepository.findByUser(user);

        List<ExamResultDTO> dtoList = results.stream().map(result -> {
            ExamResultDTO dto = new ExamResultDTO();
            dto.setScore(result.getScore());
            dto.setTimestamp(result.getTimestamp());
            dto.setExamName(result.getExam().getName());
            dto.setSubject(result.getExam().getSubject());
            return dto;
        }).toList();

        return ResponseEntity.ok(dtoList);
    }
}
