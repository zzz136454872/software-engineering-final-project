import{D as u}from"./vendor.ef13a948.js";import{u as a}from"./index.f9b84f9a.js";const s=a(),o=u("user",{state:()=>({id:-1,role:"",username:"",password:"",name:"",gender:"",jobNum:"",class:"",title:"",major:"",email:"",resume:""}),actions:{async signIn(){const e={data:{id:0,role:"\u5B66\u751F",username:"\u8D26\u53F7",password:"",name:"\u59D3\u540D",gender:"\u7537",jobNum:"\u5B66/\u5DE5\u53F7",class:"\u73ED\u7EA7",title:"\u804C\u79F0",major:"\u4E3B\u4FEE\u4E13\u4E1A/\u7814\u7A76\u65B9\u5411",email:"\u90AE\u7BB1",resume:"\u4E2A\u4EBA\u7B80\u4ECB"}};Object.assign(this.$state,e.data),s.$patch({online:!0})},async signOut(){this.$reset(),s.$patch({online:!1})},async updatePassword(e,t){},async updateDetails(){}}});export{o as u};