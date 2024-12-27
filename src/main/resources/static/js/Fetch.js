export function likeFetch(postId) {
    return fetch("/swipe/api/toggleLike", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ postId: postId })
    })
        .then(response => {
            if (!response.ok) {
                // 로그인되지 않은 경우
                const result = confirm("로그인이 필요한 서비스입니다. 로그인 창으로 이동하시겠습니까?");
                if(result){
                    window.location.href = "http://localhost:8080/login";
                    throw new Error("Unauthorized");
                } else {
                    throw new Error("Unauthorized");
                }
            }
            return response.text(); // 응답을 텍스트로 변환
        });
}


export function removeFetch(postId,commentId) {
    return fetch("/post/api/commentDelete", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            postId: postId,
            commentId: commentId}
        )
    })
        .then(response => {
            if (!response.ok) {
                // 로그인되지 않은 경우
                const result = confirm("로그인이 필요한 서비스입니다. 로그인 페이지로 이동하시겠습니까?");
                if(result){
                    window.location.href = "http://localhost:8080/login";
                    throw new Error("Unauthorized");
                } else {
                    throw new Error("Unauthorized");
                }
            }
            return response.text(); // 응답을 텍스트로 변환
        });
}

export function editFetch(editText,commentId) {
    return fetch("/post/api/commentEdit", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            commentId: commentId,
            editText: editText
        })
    })
        .then(response => {
            if (!response.ok) {
                // 로그인되지 않은 경우
                const result = confirm("로그인이 필요한 서비스입니다. 로그인 페이지로 이동하시겠습니까?");
                if(result){
                    window.location.href = "http://localhost:8080/login";
                    throw new Error("Unauthorized");
                } else {
                    throw new Error("Unauthorized");
                }
            }
            return response.text(); // 응답을 텍스트로 변환
        });
}
