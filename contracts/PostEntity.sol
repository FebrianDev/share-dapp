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

    struct Like {
        uint likeId;
        uint postId;
        address userId;
    }

    mapping(uint => Like) public likes;
    uint public likeIdCounter;

    Like [] listLike;

    Post public removeMe;

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

    function getLike(uint _postId) public view returns (address) {
        return (likes[_postId].userId);
    }

    function addAddressInLike(uint _postId, address _userId) public {
        likeIdCounter++;
        likes[likeIdCounter] = Like(likeIdCounter, _postId, _userId);
        listLike.push(Like(likeIdCounter, _postId, _userId));
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

}