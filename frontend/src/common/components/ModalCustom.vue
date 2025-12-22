<template>
  <div v-if="visible" class="custom-modal">
    <div class="custom-modal__panel">
      <div class="custom-modal__header">
        <h5 class="custom-modal__title">{{ title }}</h5>
        <button class="custom-modal__close" @click="cancel()">×</button>
      </div>

      <div class="custom-modal-body">
        <p class="modal-dup__single">{{ message }}</p>
        <input 
          v-if="isPrompt"
          v-model="promptValue"
          class="prompt-input"
          type="text"
          :placeholder="promptPlaceholder"
        />
      </div>

      <div class="custom-modal__footer">
        <button v-if="isAlert" class="btn-ok p-ripple" @click="resolve(true)">ACEPTAR</button>

        <template v-if="isConfirm">
          <button v-if="confirmButtons.visible.cancel" class="btn-outline p-ripple" @click="resolve('cancel')">{{ confirmButtons.cancel }}</button>
          <button v-if="confirmButtons.visible.no" class="btn-outline p-ripple" @click="resolve('no')">{{ confirmButtons.no }}</button>
          <button v-if="confirmButtons.visible.yes" class="btn-ok p-ripple" @click="resolve('yes')">{{ confirmButtons.yes }}</button>
        </template>

        <template v-if="isPrompt">
          <button class="btn-outline p-ripple" @click="resolve(null)">CANCELAR</button>
          <button class="btn-ok p-ripple" @click="resolve(promptValue)">ACEPTAR</button>
        </template>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      visible: false,
      title: "",
      message: "",
      isAlert: false,
      isConfirm: false,
      isPrompt: false,
      promptValue: "",
      promptPlaceholder: "",
      confirmButtons: { yes:"SI", no:"NO", cancel:"CANCELAR" },
    };
  },
  methods: {
    openBase(config) {
      this.visible=true;
      this.title=config.title||"";
      this.message=config.message||"";
      if(config.dark) document.body.classList.add('dark'); else document.body.classList.remove('dark');
      return new Promise(resolve => { this.resolver = resolve; });
    },
    alert({title="Alerta", message, dark=false}) {
      this.isAlert=true; this.isConfirm=false; this.isPrompt=false;
      return this.openBase({title,message,dark});
    },
    confirm({title="Confirmación", message, dark=false, buttons={}}) {
      this.isAlert=false; this.isConfirm=true; this.isPrompt=false;
      const visibles=buttons.visible||["yes","no","cancel"];
      this.confirmButtons = {
        yes: buttons.yes||"SI",
        no: buttons.no||"NO",
        cancel: buttons.cancel||"CANCELAR",
        visible: { yes:visibles.includes("yes"), no:visibles.includes("no"), cancel:visibles.includes("cancel") }
      };
      return this.openBase({title,message,dark});
    },
    prompt({title="INGRESAR VALOR", message, placeholder="", dark=false}) {
      this.isAlert=false; this.isConfirm=false; this.isPrompt=true;
      this.promptValue=""; this.promptPlaceholder=placeholder;
      return this.openBase({title,message,dark});
    },
    resolve(value) { this.visible=false; if(this.resolver){this.resolver(value); this.resolver=null;} },
    cancel() { if(this.isConfirm)this.resolve(false); else if(this.isPrompt)this.resolve(null); else this.resolve(true); }
  }
};
</script>

<style scoped>
.custom-modal { position: fixed; inset:0; display:grid; place-items:center; z-index:10000; background: rgba(0,0,0,0.45); }
.custom-modal__panel { width:min(400px,90vw); background: var(--p-surface-0); border-radius:12px; box-shadow:0 20px 60px rgba(0,0,0,0.25); overflow:hidden; animation: modalIn 0.25s ease-out;}
@keyframes modalIn { from{opacity:0;transform:scale(0.9);} to{opacity:1;transform:scale(1);} }

.custom-modal__header { display:flex; align-items:center; justify-content:space-between; padding:12px 16px; color: var(--p-text-color); background: linear-gradient(135deg,var(--p-primary-400),var(--p-primary-600)); box-shadow: inset 0 -2px 4px rgba(0,0,0,0.1);}
.custom-modal__title { font-weight:600; font-size:1rem; margin:0; }
.custom-modal__close { border:none; background:transparent; color: var(--p-text-color); font-size:26px; cursor:pointer; line-height:1; }

.custom-modal-body { padding:16px; color: var(--p-text-color); text-align:center; }
.modal-dup__single { margin:0; font-size:0.95rem; line-height:1.55; white-space:pre-line; }
.prompt-input { width:90%; margin-top:10px; padding:8px 10px; border-radius:6px; border:1px solid var(--p-border-color); font-size:0.9rem; }

.custom-modal__footer { padding:12px; display:flex; justify-content:center; gap:10px; }
</style>
