const Web3 = require('web3')
//const contractAddress = "0x667Bc64E953B4a74c8e1C82Cd3F5dfF4319b701F"
//const rpcEndpoint = 'https://rpc-mumbai.maticvigil.com/v1/ec8028168028b74df12bcb90c375cde51a632962'

const contractAddress = "0x9C0EEe5c6ACc75F83e1bC427417103d2FbC7E010"
const rpcEndpoint = "http://127.0.0.1:5555"

const abi = require("./build/contracts/PostEntity.json").abi
const web3 = new Web3(new Web3.providers.HttpProvider(rpcEndpoint))

const contractInstance = new web3.eth.Contract(abi, contractAddress)

const express = require('express')
const app = express()
app.use(express.json())

web3.eth.net.isListening()
    .then(() => console.log('Connected to MetaMask'))
    .catch(err => console.error('Error connecting to MetaMask:', err));

web3.eth.getAccounts()
    .then(accounts => console.log('MetaMask accounts:', accounts))
    .catch(err => console.error('Error fetching accounts:', err));

//Connect to metamask
/*app.post('/:user/signature', (req, res) => {
    // Get user from db

    // Convert msg to hex string
    const msgHex = ethUtil.bufferToHex(Buffer.from(msg));

    // Check if signature is valid
    const msgBuffer = ethUtil.toBuffer(msgHex);
    const msgHash = ethUtil.hashPersonalMessage(msgBuffer);
    const signatureBuffer = ethUtil.toBuffer(req.body.signature);
    const signatureParams = ethUtil.fromRpcSig(signatureBuffer);
    const publicKey = ethUtil.ecrecover(
        msgHash,
        signatureParams.v,
        signatureParams.r,
        signatureParams.s
    );
    const addresBuffer = ethUtil.publicToAddress(publicKey);
    const address = ethUtil.bufferToHex(addresBuffer);

    // Check if address matches
    if (address.toLowerCase() === req.params.user.toLowerCase()) {
        // Change user nonce
        const nonce = Math.floor(Math.random() * 1000000);
        res.send(true)
    } else {
        // User is not authenticated
        res.status(401).send('Invalid credentials');
    }


})*/

//Create Post
app.post('/post', async (req, res) => {
    try {
        const {userId, content, timestamp} = req.body
        const accounts = await web3.eth.getAccounts()
        await contractInstance.methods.createPost(userId, content, timestamp).send({
            from: accounts[0], gas: 4500000,
        })
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
            userId: post,
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
        const data = await contractInstance.methods.addAddressInLike(postId, userId).send({
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
app.put('/post/:id', async (req, res) => {   //http://localhost:3000/products/1
    try {
        const id = req.params.id
        const {like} = req.body
        const accounts = await web3.eth.getAccounts()
        await contractInstance.methods.updateLikeInPost(id, like).send({from: accounts[0], gas: 4500000,})
        res.json({error: false, message: "Success AUpdate Like in Post"})
    } catch (e) {
        res.status(500).send({
            error: true, message: e.message
        })
    }
})

//Delete Data
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

const port = 3003
app.listen(port, () => {
    console.log("API server is listening on port 3003")
})