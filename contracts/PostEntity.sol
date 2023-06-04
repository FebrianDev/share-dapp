// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;

contract PostEntity {

    struct Post {
        uint256 postId;
        string userId;
        string content;
        int likes;
        uint comments;
        uint timestamp;
    }

    mapping(uint256 => Post) public posts;
    uint256 public postIdCounter;

    Post [] listPost;

    Post public removeMe;

    struct Like {
        uint postId;
        address userId;
    }

    mapping(uint => Like) public likes;

    struct Comment {
        uint commentId;
        uint postId;
        string userId;
        string content;
        uint timestamp;
    }

    mapping(uint => Comment []) public comments;

    uint256 public commentIdCounter;

    Comment [] listComment;
    Comment [] newComment;

    Comment public removeComment;

    function createPost(string memory _userId, string memory _content, uint _timestamp) public {
        postIdCounter++;
        posts[postIdCounter] = Post(postIdCounter, _userId, _content, 0, 0, _timestamp);
        listPost.push(Post(postIdCounter, _userId, _content, 0, 0, _timestamp));
    }

    function getPost(uint256 _postId) public view returns (uint256, string memory, string memory, int, uint, uint) {
        require(_postId <= postIdCounter, "Invalid post ID");
        return (posts[_postId].postId, posts[_postId].userId, posts[_postId].content, posts[_postId].likes, posts[_postId].comments, posts[_postId].timestamp);
    }

    function getAllPost() public view returns (Post[] memory){
        return listPost;
    }

    function getLike(uint _postId) public view returns (address, uint) {
        return (likes[_postId].userId, _postId);
    }

    function addAddressInLike(uint _postId, address _userId) public {
        likes[_postId] = Like(_postId, _userId);
    }

    function updateLikeInPost(uint _id, int like) public {
        require(posts[_id].postId != 0, "Post is not available");

        Post storage newPostMap = posts[_id];
        newPostMap.likes = newPostMap.likes + like;

        for (uint i = 0; i < listPost.length; i++) {
            if (listPost[i].postId == _id) {
                Post storage newPost = listPost[i];
                newPost.likes = newPost.likes + like;
                break;
            }
        }
    }

    function updateCommentInPost(uint _id, uint256 comment) public {
        require(posts[_id].postId != 0, "Post is not available");

        Post storage newPostMap = posts[_id];
        newPostMap.comments = newPostMap.comments + comment;

        for (uint i = 0; i < listPost.length; i++) {
            if (listPost[i].postId == _id) {
                Post storage newPost = listPost[i];
                newPost.comments = newPost.comments + comment;
                break;
            }
        }
    }

    function deleteLike(uint256 _id) public {
        delete likes[_id];
    }

    function deletePost(uint256 _id) public {
        require(posts[_id].postId != 0, "Post is not available");
        delete posts[_id];
        for (uint i = 0; i < listPost.length; i++) {
            if (listPost[i].postId == _id) {
                removeMe = listPost[i];
                listPost[i] = listPost[listPost.length - 1];
                listPost[listPost.length - 1] = removeMe;
            }
        }
        listPost.pop();
    }

    function createComment(uint _postId, string memory _userId, string memory _content, uint _timestamp) public {
        commentIdCounter++;
        listComment.push(Comment(commentIdCounter, _postId, _userId, _content, _timestamp));
    }

    function getComment(uint _postId) public {
        clearNewComment();

        for (uint i = 0; i < listComment.length; i++) {
            if (listComment[i].postId == _postId) {
                Comment storage c = listComment[i];
                newComment.push(c);
            }
        }
    }

    function getComment() public view returns (Comment [] memory) {
        return newComment;
    }

    function clearNewComment() private {
        for (uint i = 0; i < newComment.length; i++) {
            newComment.pop();
        }
    }

    function deleteComment(uint256 _id) public {
        for (uint i = 0; i < listComment.length; i++) {
            if (listComment[i].commentId == _id) {
                removeComment = listComment[i];
                listComment[i] = listComment[listComment.length - 1];
                listComment[listComment.length - 1] = removeComment;
            }
        }
        listComment.pop();
    }
}