package est.commitdate.entity;


import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Test")
@NoArgsConstructor
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name = "test_Id")
    private int testId;

    @Column(name = "test_Name")
    private String testName;

    @Column(name = "test_Description")
    private String testDescription;

    @Column(name="testDateTime")
    private LocalDateTime testDateTime = LocalDateTime.now();;

    @Column(name = "status", nullable = false , columnDefinition = "INT DEFAULT 1")
    private int status = 1;

}
