<script setup lang="ts">
// import type { SanGuo, StageCfg } from '@/app/sg';
import type { SanGuo } from '@/app/sg';
import type { StageCfg } from '@/app/stage';
import { inject, onMounted, onUnmounted, ref, watch } from 'vue';
// import PBtn from '../comps/PBtn.vue';
import { CfgKey, CfgType } from '@/app/cfg';
import Bg from '@/components/comps/Bg.vue';
import Text from '@/components/comps/Text.vue';
import TextInput from '@/components/comps/TextInput.vue';
import { actionMgr } from '@/app/action';
import PBtn from '../comps/PBtn.vue';
import Img from '../comps/Img.vue';
import ProvincesMap from '../comps/regplay/ProvincesMap.vue';
import Sct from '../comps/Sct.vue';
import KeyValue from '../comps/KeyValue.vue';
import CitySelect from '../comps/playing/CitySelect.vue';
import InnerMain from '../comps/playing/InnerMain.vue';

// import { actionMgr } from '@/app/actionMgr';
// import Img from '../comps/Img.vue';
// import Sct from '../comps/Sct.vue';
// import ProvincesMap from '../comps/regplay/ProvincesMap.vue';
// import ChatPan from '../comps/playing/ChatPan.vue';
// import KeyValue from '../comps/KeyValue.vue';
// import CitySelect from '../comps/playing/CitySelect.vue';
// import CityRes from '../comps/playing/CityRes.vue';
// import InnerMain from '../comps/playing/InnerMain.vue';
// import PBtn from './comps/PBtn.vue';


const { sg } = inject('sg') as { sg: SanGuo }
// const t = ref('K:playing#army;S:20,30,100;RFI:common#btn_osct;SCTABLE;')
// const t = ref('K:playing#army;S:20,30,100;RFI:playing#topbutton_army;T:I_PTN')
const stage = ref<StageCfg>()

watch(sg.stageMgr.stage, async (newStage, oldStage) => {
    console.log(`stageView: watch newStage: ${newStage},oldStage: ${oldStage}`)

    const oldStageCfg = sg.stageMgr.getStageCfg(oldStage)
    oldStageCfg && await actionMgr.onUnmounted(oldStageCfg)

    const newStageCfg = sg.stageMgr.getStageCfg(newStage)
    newStageCfg && await actionMgr.onMounted(newStageCfg)
    stage.value = newStageCfg
}, { immediate: true })

onMounted(() => { sg.stageMgr.testStage() })

onUnmounted(async () => {
    stage.value && await actionMgr.onUnmounted(stage.value)
})
</script>
<template>
    <div class="sg-playing">
        <template v-if="stage && stage.info">
            <template v-for="comp in stage.info.cfgs" :key="comp.key()">
                <Text v-if="comp.type() == CfgType.text" :cfg="comp" v-show="comp.show()" :id="comp.key()" />
                <Bg v-else-if="comp.type() == CfgType.bg" :cfg="comp" v-show="comp.show()" :id="comp.key()" />
                <PBtn v-else-if="comp.isBtn()" :cfg="comp" v-show="comp.show()" :id="comp.key()" />
                <TextInput v-else-if="comp.type() == CfgType.textInput" :cfg="comp" v-show="comp.show()"
                    :id="comp.key()" />
                <Img v-else-if="comp.isImg()" :cfg="comp" v-show="comp.show()" :id="comp.key()" />
                <Sct v-else-if="comp.type() == CfgType.select" :cfg="comp" v-show="comp.show()" :id="comp.key()" />
                <KeyValue v-else-if="comp.type() == CfgType.keyValue" :cfg="comp" v-show="comp.show()"
                    :id="comp.key()" />
                <template v-else-if="comp.type() == CfgType.customComp" v-show="comp.show()">
                    <ProvincesMap v-if="comp.show() && comp.refComp() == 'provincesMap'" :cfg="comp" :id="comp.key()" />
                    <CitySelect v-if="comp.show() && comp.refComp() == 'citySelect'" :cfg="comp" :id="comp.key()" />
                    <InnerMain v-if="comp.show() && comp.refComp() == 'innerMain'" :cfg="comp" :id="comp.key()" />
                    <!-- <ChatPan v-if="comp.show() && comp.refComp() == 'chatPan'" :cfg="comp" :id="comp.key()" />
                    <CityRes v-if="comp.show() && comp.refComp() == 'cityRes'" :cfg="comp" :id="comp.key()" />
                     -->
                </template>
                <div v-else>{{ comp.cfg }}</div>
            </template>
        </template>
    </div>
</template>
<style lang="less" scoped>
.sg-playing {
    position: absolute;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    overflow: hidden;
    color: antiquewhite;
}
</style>