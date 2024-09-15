import { ethers } from "hardhat"
import type { Token__factory } from "../../types/factories/contracts/Token__factory"
import { Token } from "../../types"
import { loadFixture } from "@nomicfoundation/hardhat-network-helpers"

describe("Token ERC20", function () {
    async function deployTokenFixture() {
        const [owner, addr1, addr2] = await ethers.getSigners();
        const tokenName = "Hello Pin"
        const symbol = "PIN"
        const supply = 10000

        const tokenFactory: Token__factory = (await ethers.getContractFactory("Token")) as Token__factory
        const token: Token = (await tokenFactory.deploy(tokenName, symbol, supply)) as Token
        const tokenAddress = await token.getAddress()

        return { tokenName, symbol, supply, token, owner, tokenAddress, addr1, addr2 };
    }

    it.skip("ERC20 query token name", async () => {
        const { token, tokenName } = await loadFixture(deployTokenFixture);

        const name = await token.name()
        console.log(`token name: ${name}, origin name: ${tokenName}`)
    })

    it.skip("ERC20 query token symbol", async () => {
        const { token, symbol } = await loadFixture(deployTokenFixture);

        const sym = await token.symbol()
        console.log(`token symbol name: ${sym}, origin symbol: ${symbol}`)
    })

    it.skip("ERC20 query token supply", async () => {
        const { token, supply } = await loadFixture(deployTokenFixture);

        const totalSupply = await token.totalSupply()
        const unit = await token.decimals()
        console.log(`token supply: ${totalSupply}, unit: ${unit}, origin supply: ${supply}`)
    })

    it.skip("ERC20 token decimals unit", async () => {
        const { token } = await loadFixture(deployTokenFixture);

        const unit = await token.decimals()
        console.log(`token decimal unit: ${unit}`)
    })

    it.skip("ERC20 query token balanceOf", async () => {
        const { token, owner, addr1 } = await loadFixture(deployTokenFixture);

        const ownerBalance = await token.balanceOf(owner.address)
        const addr1Balance = await token.balanceOf(addr1.address)

        console.log(`token owner balance: ${ownerBalance}, addr balance: ${addr1Balance}`)
    })

    it.skip("ERC20 transfer token", async () => {
        const { token, owner, addr1 } = await loadFixture(deployTokenFixture);

        const res = await token.transfer(addr1.address, 10)
        const ownerBalance = await token.balanceOf(owner.address)
        const addr1Balance = await token.balanceOf(addr1.address)

        console.log(`token owner balance: ${ownerBalance}, transfer result: ${res}, addr balance: ${addr1Balance}`)
    })

    //allowance：返回_spender还能提取token的个数
    it.skip("ERC20 allowance token", async () => {
        const { token, owner, addr1 } = await loadFixture(deployTokenFixture);

        const res = await token.allowance(owner.address, addr1.address)
        const ownerBalance = await token.balanceOf(owner.address)
        const addr1Balance = await token.balanceOf(addr1.address)

        console.log(`token owner balance: ${ownerBalance}, allowance result: ${res}, addr balance: ${addr1Balance}`)
    })

    it.skip("ERC20 approve token", async () => {
        const { token, owner, addr1 } = await loadFixture(deployTokenFixture);

        const appRes = await token.approve(addr1.address, 20)
        const ownerBalance = await token.balanceOf(owner.address)
        const addr1Balance = await token.balanceOf(addr1.address)

        const res = await token.allowance(owner.address, addr1.address)

        console.log(`token owner balance: ${ownerBalance}, approve result: ${appRes}, allowance: ${res}, addr balance: ${addr1Balance}`)
    })

    it.skip("ERC20 approve/transfer token", async () => {
        const { token, owner, addr1, addr2 } = await loadFixture(deployTokenFixture);
        console.log(`owner address: ${owner.address}, addr1: ${addr1.address}, add2: ${addr2.address}`)

        const appRes = await token.approve(addr1.address, 20)
        const res = await token.allowance(owner.address, addr1.address)
        //连接addr1账户, 然后转账给addr2账户
        const transRes = await token.connect(addr1).transferFrom(owner.address, addr2.address, 10);

        const ownerBalance = await token.balanceOf(owner.address)
        const addr1Balance = await token.balanceOf(addr1.address)
        const addr2Balance = await token.balanceOf(addr2.address)

        const allAfter = await token.allowance(owner.address, addr1.address)
        console.log(`addr1 before token: ${res}, after: ${allAfter}`)

        console.log(`token owner balance: ${ownerBalance}, approve result: ${appRes}, allowance: ${res}, transRes: ${transRes}, addr1 balance: ${addr1Balance},addr2 balance: ${addr2Balance}`)
    })

    it.skip("ERC20 increaseAllowance token", async () => {
        const { token, owner, addr1, addr2 } = await loadFixture(deployTokenFixture);
        console.log(`owner address: ${owner.address}, addr1: ${addr1.address}, add2: ${addr2.address}`)

        await token.approve(addr1.address, 20)
        const allBefore = await token.allowance(owner.address, addr1.address)

        await token.increaseAllowance(addr1.address, 10)
        const allAfter = await token.allowance(owner.address, addr1.address)

        console.log(`token addr1 approve token: ${allBefore}, after: ${allAfter}`)
    })

    it.skip("ERC20 decreaseAllowance token", async () => {
        const { token, owner, addr1, addr2 } = await loadFixture(deployTokenFixture);
        console.log(`owner address: ${owner.address}, addr1: ${addr1.address}, add2: ${addr2.address}`)

        await token.approve(addr1.address, 30)
        const allBefore = await token.allowance(owner.address, addr1.address)

        await token.decreaseAllowance(addr1.address, 15)
        const allAfter = await token.allowance(owner.address, addr1.address)

        console.log(`token addr1 approve token: ${allBefore}, after: ${allAfter}`)
    })

    // ERC20Burnable
    it.skip("ERC20 burn token", async () => {
        const { token, owner, addr1, addr2 } = await loadFixture(deployTokenFixture);
        console.log(`owner address: ${owner.address}, addr1: ${addr1.address}, add2: ${addr2.address}`)

        const beforeBalance = await token.balanceOf(owner.address)
        await token.burn(10)
        const afterBalance = await token.balanceOf(owner.address)

        console.log(`token owner before balance: ${beforeBalance}, after: ${afterBalance}`)
    })

    it.skip("ERC20 burnFrom token", async () => {
        const { token, owner, addr1, addr2 } = await loadFixture(deployTokenFixture);
        console.log(`owner address: ${owner.address}, addr1: ${addr1.address}, add2: ${addr2.address}`)

        await token.approve(addr1.address, 100)
        const beforeBalance = await token.allowance(owner.address, addr1.address)
        await token.connect(addr1).burnFrom(owner.address, 20)
        const afterBalance = await token.allowance(owner.address, addr1.address)

        console.log(`token owner before balance: ${beforeBalance}, after: ${afterBalance}`)
    })
})
