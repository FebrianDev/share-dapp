const Web3 = require('web3')
//const contractAddress = "0xA72d23D919Ddb3d29c1894D50650043d0A5b2c33"
//const rpcEndpoint = 'https://matic-mumbai.chainstacklabs.com'

const contractAddress = "0xd3F6EEb3e5A48605B84B20260147a8979D76F68B"
const rpcEndpoint = "http://127.0.0.1:5555"

const abi = require("./build/contracts/PostEntity.json").abi
const web3 = new Web3(new Web3.providers.HttpProvider(rpcEndpoint))

const contractInstance = new web3.eth.Contract(abi, contractAddress)

const express = require('express')
const app = express()

app.use(express.json())

//Get Accounts
app.get("/account", async (req, res) => {
    try {
        web3.eth.net.isListening()
            .then(() => console.log('Connected to MetaMask'))
            .catch(err => console.error('Error connecting to MetaMask:', err))

        const accounts = await web3.eth.getAccounts()
        console.log(accounts)

        res.status(200).send({
            error: false, message: "Success Get Accounts", result: accounts
        })
    } catch (e) {
        res.status(500).send({
            error: true, message: e.message
        })
    }
})

//Create Post
app.post('/post', async (req, res) => {
    try {
        const {userId, content, timestamp} = req.body
        const accounts = await web3.eth.getAccounts()
        const data = await contractInstance.methods.createPost(userId, content, timestamp).send({
            from: accounts[0], gas: 4500000,
        })
        console.log(data)
        res.status(200).send({
            error: false, message: "Success Create Post"
        })
    } catch (e) {
        res.status(500).send({
            error: true, message: e.message
        })
    }
})

//Get All Post
app.get('/post', async (req, res) => {
    try {
        const allPost = await contractInstance.methods.getAllPost().call()
        const post = allPost.map(post => ({
            postId: parseInt(post.postId),
            userId: post.userId,
            content: post.content,
            likes: post.likes,
            comments: post.comments,
            timestamp: post.timestamp
        }))
        res.send({
            error: false, message: "Success Get All Post", result: post
        })
    } catch (e) {
        res.status(500).send({
            error: true, message: e.message
        })
    }
})

//Get PostById
app.get('/post/:id', async (req, res) => {
    try {
        console.log(req.params.id)
        const post = await contractInstance.methods.getPost(req.params.id).call()
        const result = {
            postId: parseInt(post[0]),
            userId: post[1],
            content: post[2],
            likes: post[3],
            comments: post[4],
            timestamp: post[5]
        }
        res.send({
            error: false, message: "Success Get Post By Id", result: result
        })
    } catch (e) {
        res.status(500).send({
            error: true, message: e.message
        })
    }
})

//Get Like ById User
app.get('/like/:id', async (req, res) => {
    try {
        console.log(req.params.id)
        const post = await contractInstance.methods.getLike(req.params.id).call()
        const result = {
            userId: post[0],
            postId: post[1]
        }
        res.send({
            error: false, message: "Success Get Like By Id", result: result
        })
    } catch (e) {
        res.status(500).send({
            error: true, message: e.message
        })
    }
})

// Add Address In Like
app.post('/like', async (req, res) => {
    try {
        const {postId, userId} = req.body
        const accounts = await web3.eth.getAccounts()
        await contractInstance.methods.addAddressInLike(postId, userId).send({
            from: accounts[0], gas: 4500000,
        })
        res.status(200).send({
            error: false, message: "Success Add Address in Like"
        })
    } catch (e) {
        res.status(500).send({
            error: true, message: e.message
        })
    }
})

//Update Like in Post
app.put('/post-like/:id', async (req, res) => {   //http://localhost:3000/products/1
    try {
        const id = req.params.id
        const {like} = req.body
        const accounts = await web3.eth.getAccounts()
        await contractInstance.methods.updateLikeInPost(id, like).send({from: accounts[0], gas: 4500000,})
        res.json({error: false, message: "Success Update Like in Post"})
    } catch (e) {
        res.status(500).send({
            error: true, message: e.message
        })
    }
})

//Update comment in Post
app.put('/post-comment/:id', async (req, res) => {   //http://localhost:3000/products/1
    try {
        const id = req.params.id
        const {comment} = req.body
        const accounts = await web3.eth.getAccounts()
        await contractInstance.methods.updateCommentInPost(id, comment).send({from: accounts[0], gas: 4500000,})
        res.json({error: false, message: "Success Update Like in Post"})
    } catch (e) {
        res.status(500).send({
            error: true, message: e.message
        })
    }
})

//Delete Like in Post
app.delete('/like/:id', async (req, res) => {
    try {
        //PostId
        const id = req.params.id
        console.log(id)
        const accounts = await web3.eth.getAccounts()
        await contractInstance.methods.deleteLike(id).send({from: accounts[0], gas: 4500000,})

        res.json({error: false, message: "Success Delete Like"})
    } catch (e) {
        res.status(500).send({
            error: true, message: e.message
        })
    }
})

//Delete Post
app.delete('/post/:id', async (req, res) => {
    try {
        const id = req.params.id
        const accounts = await web3.eth.getAccounts()
        await contractInstance.methods.deletePost(id).send({from: accounts[0], gas: 4500000,})

        res.json({error: false, message: "Success Delete Post"})
    } catch (e) {
        res.status(500).send({
            error: true, message: e.message
        })
    }
})

app.post('/comment', async (req, res) => {
    try {
        console.log("TEST")
        const {postId, userId, content, timestamp} = req.body
        const accounts = await web3.eth.getAccounts()
        const data = await contractInstance.methods.createComment(postId, userId, content, timestamp).send({
            from: accounts[0], gas: 4500000,
        })

        console.log(data)
        res.status(200).send({
            error: false, message: "Success Create Comment"
        })
    } catch (e) {
        res.status(500).send({
            error: true, message: e.message
        })
    }
})

//Get Comment
app.get('/comment/:postId', async (req, res) => {
    try {
        console.log(req.params.postId)
        const accounts = await web3.eth.getAccounts()
        const data = await contractInstance.methods.getComment(req.params.postId).send({
            from: accounts[0], gas: 4500000,
        })
        console.log(data)
        const comments = await contractInstance.methods.getComment().call()
        const result = comments.map(comment => ({
            commentId: parseInt(comment.commentId),
            postId: comment.postId,
            userId: comment.userId,
            content: comment.content,
            timestamp: comment.timestamp
        }))

        console.log(comments)
        res.send({
            error: false, message: "Success Get Comment", result: result
        })
    } catch (e) {
        res.status(500).send({
            error: true, message: e.message
        })
    }
})

//Delete Comment
app.delete('/comment/:id', async (req, res) => {
    try {
        const id = req.params.id
        const accounts = await web3.eth.getAccounts()
        await contractInstance.methods.deleteComment(id).send({from: accounts[0], gas: 4500000,})

        res.json({error: false, message: "Success Delete Comment"})
    } catch (e) {
        res.status(500).send({
            error: true, message: e.message
        })
    }
})


const port = 3003
app.listen(port, () => {
    console.log("API server is listening on port 3003")
})