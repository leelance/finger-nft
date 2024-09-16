import { createApp } from "vue";
import App from "@/App.vue";
import router from "@/router";
import i18n from "@/i18n/i18n";
import store from "@/store";
import "lib-flexible/flexible";
import api from "@/api/index";
import tools from "@/util/tools";
import web3 from "@/util/web3/index";
import NoContent from "@/components/NoContent.vue";
import NFTItem from "@/components/NFTItem.vue";
import NFTItemLoad from "@/components/loading/NFTItemLoad.vue";
import FollowLoad from "@/components/loading/FollowLoad.vue";
import LoadStatus from "@/components/LoadStatus.vue";
import Avatar from "@/components/Avatar.vue";
import sdk from "@/util/sdk/index";
import Window from "@/components/Window.vue";


import ProfilePopover from "@/components/ProfilePopover.vue";
import Placeholder from "@/components/Placeholder.vue";

import SaleDialog from "@/components/dialogs/Sale.vue";
import CancelSaleDialog from "@/components/dialogs/CancelSale.vue";
import BuyDialog from "@/components/dialogs/Buy.vue";

import BidDialog from "@/components/dialogs/Bid.vue";
import CancelBidDialog from "@/components/dialogs/CancelBid.vue";
import AcceptDialog from "@/components/dialogs/Accept.vue";

import TransferDialog from "@/components/dialogs/Transfer.vue";
import BurnDialog from "@/components/dialogs/Burn.vue";
import NoFound from "@/components/NoFound.vue";

import "@/styles/myicon/iconfont.css";
import "element-plus/dist/index.css";

import { VueClipboard } from "@soerenmartius/vue3-clipboard";
import * as filters from "@/filters";

import ElementPlus from "element-plus";
import "./permission";
import "../theme/index.css";

import "@/styles/index.scss";

import "@/assets/font/font.css";

const app = createApp(App);
app.config.globalProperties.$web3 = web3;
app.config.globalProperties.$api = api;
app.config.globalProperties.$tools = tools;
app.config.globalProperties.$sdk = sdk;
app.config.globalProperties.$filters = filters;

app.use(VueClipboard);
app.use(ElementPlus);

app.component("Avatar", Avatar);
app.component("popup-window", Window);
app.component("no-content", NoContent);
app.component("nft-item", NFTItem);
app.component("nft-item-load", NFTItemLoad);
app.component("follow-load", FollowLoad);
app.component("load-status", LoadStatus);
app.component("sale-dialog", SaleDialog);
app.component("cancel-sale-dialog", CancelSaleDialog);
app.component("buy-dialog", BuyDialog);
app.component("bid-dialog", BidDialog);
app.component("cancel-bid-dialog", CancelBidDialog);
app.component("accept-dialog", AcceptDialog);
app.component("transfer-dialog", TransferDialog);
app.component("burn-dialog", BurnDialog);
app.component("profile-popover", ProfilePopover);

app.component("placeholder", Placeholder);

app.component("no-found", NoFound);

app.use(store);
app.use(router);
app.use(i18n);
app.mount("#app");

export default app;