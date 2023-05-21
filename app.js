const Web3 = require('web3');

//const contractAddress = "0xac0302cf8026c462918f4ea7f81e01dad5f672df";
//const rpcEndpoint = 'https://api-testnet.polygonscan.com/api?module=account&action=balance&address=0xaC0302Cf8026C462918f4Ea7F81E01dAd5f672DF&tag=latest&apikey=7I5FEFSWQSP8XR16JVH4Q75QYGHG4Y1D73';

const contractAddress = "0x9C0EEe5c6ACc75F83e1bC427417103d2FbC7E010"
const rpcEndpoint = "http://127.0.0.1:5555"

const abi = require("./build/contracts/PostEntity.json").abi;
const web3 = new Web3(new Web3.providers.HttpProvider(rpcEndpoint));

const contractInstance = new web3.eth.Contract(abi, contractAddress);

const express = require('express');
const app = express();
app.use(express.json());

//Create Post
app.post('/post', async (req, res) => {
    try {
        const {userId, content, timestamp} = req.body
        const accounts = await web3.eth.getAccounts();
        const data = await contractInstance.methods.createPost(userId, content, timestamp).send({
            from: accounts[0],
            gas: 4500000,
        });
        console.log(data)
        res.status(200).send(true)
    } catch (e) {
        console.log(e.message)
        res.status(500).send(e.message);
    }
})

//Get All Post
app.get('/post', async (req, res) => {
    const allPost = await contractInstance.methods.getAllPost().call();
    const post = allPost.map(post => ({
        postId: parseInt(post.postId),
        userId: post.userId,
        content: post.content,
        likes: post.likes,
        comments: post.comments,
        timestamp: post.timestamp
    }))
    console.log(post)
    res.json({post})
})

//Get PostById
app.get('/post/:id', async (req, res) => {
    try {
        console.log(req.params.id)
        const post = await contractInstance.methods.getPost(req.params.id).call();
        const result = {
            postId: parseInt(post[0]),
            userId: post[1],
            content: post[2],
            likes: post[3],
            comments: post[4],
            timestamp: post[5]
        }
        console.log(result)
        res.send({result})
    }catch (error) {
        res.status(500).send(error.message);
    }
})

//Get Like
app.get('/like/:id', async (req, res) => {
    try {
        console.log(req.params.id)
        const post = await contractInstance.methods.getLike(req.params.id).call();
        const result = {
            userId: post,
        }
        console.log(post)
        res.send({result})
    }catch (error) {
        res.status(500).send(error.message);
    }
})

// Add Address In Like
app.post('/like', async (req, res) => {
    try {
        const {postId, userId} = req.body
        const accounts = await web3.eth.getAccounts();
        const data = await contractInstance.methods.addAddressInLike(postId, userId).send({
            from: accounts[0],
            gas: 4500000,
        });
        console.log(data)
        res.status(200).send(true)
    }catch (error) {
        res.status(500).send(error.message);
    }
})

//Update Like in Post
app.put('/post/:id', async (req, res) => {   //http://localhost:3000/products/1
    try {
        const id = req.params.id;
        const {like} = req.body;
        const accounts = await web3.eth.getAccounts();
        await contractInstance.methods.updateLikeInPost(id, like).send({from: accounts[0], gas: 4500000,});
        // await tx.wait();
        res.json({success: true})
    } catch (error) {
        res.status(500).send(error.message);
    }
});

//Delete Data
app.delete('/post/:id', async (req, res) => {
    try {
        const id = req.params.id;
        const accounts = await web3.eth.getAccounts();
        await contractInstance.methods.deletePost(id).send({from: accounts[0], gas: 4500000,});

        res.json({success: true})
    } catch (error) {
        res.status(500).send(error.message);
    }
});

const port = 3003;
app.listen(port, () => {
    console.log("API server is listening on port 3003")
})