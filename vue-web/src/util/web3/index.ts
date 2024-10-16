import { Web3 } from 'web3'
import store from "@/store"
import i18n from "@/i18n/i18n"
import tools from "@/util/tools"

const promisify = (inner: any) =>
  new Promise((resolve, reject) =>
    inner((err: any, res: any) => {
      if (err) {
        reject(err);
      } else {
        resolve(res);
      }
    })
  );

export default {
  async connectWeb3() {
    var error = "";
    if (window.ethereum) {
      try {
        var t = await window.ethereum.request({
          method: "eth_requestAccounts",
        });
        if (!t) {
          error = "MetaMask enable Error";
          return { error };
        }

        const web3 = new Web3(window.ethereum);
        console.log(web3, ', web3j')

        window.wallet = web3;
        const networkId = await web3.eth.getChainId();
        const coinbase = await web3.eth.getCoinbase();

        console.log(`===>networkId: ${networkId}, coinbase: ${coinbase}`)

        window.ethereum.once("accountsChanged", this.accountsChanged);
        window.ethereum.on("chainChanged", this.chainChanged);
        window.ethereum.on("disconnect", this.disconnect);
        let walletType = "metamask";
        return { networkId, coinbase, walletType };
      } catch (e: any) {
        error = e.message;
      }
    } else {
      error = "MetaMask not Install";
    }
    return { error };
  },

  async connectWallet(type: string) {
    return await this.connectWeb3();
  },

  accountsChanged(accounts) {
    if (!store.state.connected) return;
    store.dispatch("logout");
    if (accounts.length) {
      store.dispatch("connect");
    }
  },

  chainChanged(channelId) {
    let network = store.state.config.network;
    if (parseInt(channelId) != parseInt(network.chainId)) {
      tools.messageBox(
        i18n.global.t("global.errNetwork"),
        i18n.global.t("global.changeNetworkTo") + network.name
      );
    }
  },

  disconnect(error) {
    if (!store.state.connected) return;
    store.dispatch("logout");

    if (store.state.web3.walletType == "walletconnect") {
      if (window.provider) {
        window.provider.disconnect();
      }
    }
  },

  async getTransaction(tx) {
    let web3 = this.getWeb3();
    try {
      return await promisify((cb) => web3.eth.getTransaction(tx, cb));
    } catch (e) {
      return { error: e.message };
    }
  },

  async getTransactionReceipt(tx) {
    let web3 = this.getWeb3();
    try {
      return await promisify((cb) => web3.eth.getTransactionReceipt(tx, cb));
    } catch (e) {
      return { error: e.message };
    }
  },

  async decodeLog(inputs, hexString, options) {
    let web3 = this.getWeb3();
    try {
      return await promisify((cb) =>
        web3.eth.abi.decodeLog(inputs, hexString, options, cb)
      );
    } catch (e) {
      return { error: e.message };
    }
  },
  
  getWeb3() {
    return window.wallet;
  },

  async loginWallet(address: string) {
    const timestamp = Math.round(new Date().getTime() / 1000)
    var message = store.state.config.loginMessage + " " + timestamp;
    try {
      const signature: any = await this.sign(message, address);
      if (signature.error) return signature;

      return {
        signature: signature,
        timestamp: timestamp,
      };
    } catch (e: any) {
      return { error: e.message };
    }
  },

  async sign(message: string, address: string) {
    const web3 = window.wallet;
    try {
      const checkAddress = web3?.utils.toChecksumAddress(address)
      if (checkAddress == undefined) {
        return { error: 'address check fail.' };
      }

      return await web3?.eth.personal.sign(message, checkAddress, '123456');
    } catch (e: any) {
      return { error: e.message };
    }
  },

  checkWeb3() {
    return window.ethereum && window.ethereum.isConnected();
  },
  async monitorWeb3() {
    let web3 = window.wallet;
    if (typeof web3 == "undefined" || !web3) return;
    var result = await this.checkWeb3(web3);
    if (!result) {
      return;
    }
    let network = store.state.config.network;

    var networkId = await promisify((cb) => web3.eth.getChainId(cb));
    if (networkId != network.chainId) {
      tools.messageBox(
        i18n.global.t("global.errNetwork"),
        i18n.global.t("global.changeNetworkTo") + network.name
      );
    }
  },
  async changeNetwork(network) {
    try {
      let result = await window.ethereum.request({
        method: "wallet_switchEthereumChain",
        params: [{ chainId: "0x" + network.chainId.toString(16) }],
      });
      return result;
    } catch (e) {
      if (e.code == 4001) return { error: e.message };
      try {
        let result = await window.ethereum.request({
          method: "wallet_addEthereumChain",
          params: [
            {
              chainId: "0x" + network.chainId.toString(16),
              chainName: network.name,
              nativeCurrency: {
                name: network.symbol,
                symbol: network.symbol,
                decimals: 18,
              },
              rpcUrls: [network.rpc],
            },
          ],
        });
        return result;
      } catch (e) {
        return { error: e.message };
      }
    }
  },
  async addToken(token) {
    let wasAdded = await window.ethereum.request({
      method: "wallet_watchAsset",
      params: {
        type: "ERC20",
        options: {
          address: token.address,
          symbol: token.symbol,
          decimals: token.decimals,
        },
      },
    });
  },
};
