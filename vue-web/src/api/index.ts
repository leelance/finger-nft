import request from "@/api/request";
import apiList from "@/api/apiList";
import qs from "qs";

function getApiObj(url: string, data = {}) {
  let apiArray = url.split(".");
  let api: any = apiList;
  apiArray.forEach((v) => {
    api = api[v];
  });
  return api;
}

const URL_PREFIX = "/fingernft";

export default function api(url: string, data = {}) {
  var api = getApiObj(url);

  var post = {
    url: URL_PREFIX + api.url,
    method: api.method,
  } as any;
  if (url == "storage.upload" || url === "storage.multiupload") {
    post.headers = { "Content-Type": "multipart/form-data" };
    post.data = data;
  } else if (url == "log.info") {
    post.url = api.url;
    post.data = data;
  } else {
    var method = api.method.toLowerCase();
    if (method == "post") {
      post.data = qs.stringify(data);
    } else {
      post.post = data;
    }
  }
  if (url == "oauth.authorize") {
    post.url = api.url;
  }

  return request(post);
}
