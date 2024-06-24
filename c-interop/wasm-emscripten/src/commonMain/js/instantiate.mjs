import Module from "./ffi-crypto.js"

const wasmSetup = new Promise(function (resolve, reject) {
    Module['onRuntimeInitialized'] = _ => {
        resolve(Module);
    };
});
await wasmSetup;

export default Module;