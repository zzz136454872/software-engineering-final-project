import{d as i,E as d,c as p,F as _,H as u,G as t,N as e,M as a,I as f,U as b,J as B,V as F}from"./vendor.ef13a948.js";import{x as v,y,z as x,A as E,B as C}from"./vendor-quasar.d7e21608.js";import{u as g}from"./index.f9b84f9a.js";import{u as A}from"./user.53d56c55.js";import{_ as j}from"./plugin-vue_export-helper.5a098b48.js";var n={};const r=i({setup(h){const o=d();g().online||o.push("/sign");const l=A(),c=p(()=>{switch(l.role){case"\u7BA1\u7406\u5458":return[{name:"notice",label:"\u7CFB\u7EDF\u516C\u544A"},{name:"user",label:"\u7528\u6237\u7BA1\u7406"},{name:"bysj",label:"\u8BFE\u9898\u7BA1\u7406"}];case"\u6559\u5E08":return[{name:"notice",label:"\u7CFB\u7EDF\u516C\u544A"},{name:"my-topic",label:"\u6211\u7684\u8BFE\u9898"},{name:"selected-student",label:"\u5DF2\u9009\u5B66\u751F"}];case"\u5B66\u751F":return[{name:"notice",label:"\u7CFB\u7EDF\u516C\u544A"},{name:"topic-list",label:"\u8BFE\u9898\u5217\u8868"},{name:"selected-topic",label:"\u5DF2\u9009\u8BFE\u9898"}];default:return[]}});return(Q,S)=>{const m=_("router-view");return u(),t(E,{class:"q-mx-auto ui-sys-page"},{default:e(()=>[a(v,{align:"justify",class:"ui-sys-tabs"},{default:e(()=>[(u(!0),f(F,null,b(B(c),s=>(u(),t(C,{to:`/system/${s.name}`,label:s.label},null,8,["to","label"]))),256))]),_:1}),a(x,{"model-value":"index",animated:"",class:"transparent"},{default:e(()=>[a(y,{name:"index"},{default:e(()=>[a(m)]),_:1})]),_:1})]),_:1})}}});typeof n=="function"&&n(r);var I=j(r,[["__scopeId","data-v-4c9dfd98"]]);export{I as default};