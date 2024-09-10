import { ethers, formatEther } from "ethers"

const provider = new ethers.JsonRpcProvider('http://192.168.204.140:8545')
const ownerAddress = '0x811bB0C5EB3664D7bB167Ebc1cD77322586B7f89'

async function get() {
    const blockNumber = await provider.getBlockNumber()
    console.log(`block number: ${blockNumber}`)

    const balance = await provider.getBalance(ownerAddress)
    const balanceformat = ethers.formatEther(balance)
    console.log(`balance fromat: ${balanceformat}, balance: ${balance}`)

    const txCount = await provider.getTransactionCount(ownerAddress)
    console.log(`balance tx count: ${txCount}`)

    const chainId = (await provider.getNetwork()).chainId
    console.log(`chainId: ${chainId}`)

    //const listenerCount = await provider.listAccounts()
    //console.log(listenerCount)

    // tx info
    const txInfo = await provider.getTransaction("0x1cfe5918bdd29772c0c57ae6f702c62636318dbe7f27145e3c386aeaa67a8bca")
    console.log(txInfo)

    // tx receipt
    const txreceipt = await provider.getTransactionReceipt("0x1cfe5918bdd29772c0c57ae6f702c62636318dbe7f27145e3c386aeaa67a8bca")
    console.log(txreceipt)

    // block info
    const blockInfo = await provider.getBlock(0)
    console.log(blockInfo)
}

//get()

async function transfer() {
    const privatekey = "0x2d610c01187ae1ca8d93aeef276ade47c5e1e2df307e09d815f1db0ed0065fd6";
    const wallet = new ethers.Wallet(privatekey, provider);

    const toAddress = '0x6bC312AB13A6B7001F5C2E81F9705E9083659D78';
    const balance = await provider.getBalance(toAddress)
    console.log(`to address balance: ${balance}`)

    const value = ethers.parseEther('10');

    const tx = {
        to: toAddress,
        value: value,
        gasLimit: 21000,
        gasPrice: ethers.parseUnits("10", "gwei"),
        chainId: 999,
        nonce: await provider.getTransactionCount(wallet.address)
    };

    const signedTx = await wallet.signTransaction(tx);

    // 发送交易
    //const req = TransactionRequest;
    //wallet.sendTransaction(signedTx)
}

//transfer()