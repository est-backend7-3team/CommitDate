package est.commitdate.entity;


import jakarta.persistence.*;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;


//    private Member writer;
//
//    private Integer status;

}
