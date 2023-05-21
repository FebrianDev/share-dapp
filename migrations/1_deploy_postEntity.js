const PostEntity = artifacts.require("./PostEntity")

module.exports = function (deployer) {
    deployer.deploy(PostEntity)
}