<script setup lang="ts">
import type { SanGuo } from '@/app/sg';
import { inject, onMounted, onUnmounted, ref, watch } from 'vue';
import { CfgKey, CfgType } from '@/app/cfg';
import Bg from '@/components/comps/Bg.vue';
import Text from '@/components/comps/Text.vue';
import PBtn from '../comps/PBtn.vue';
import TextInput from '../comps/TextInput.vue';
import Img from '../comps/Img.vue';
import Sct from '../comps/Sct.vue';
import KeyValue from '../comps/KeyValue.vue';
import ProvincesMap from '../comps/regplay/ProvincesMap.vue';
import type { Stage } from '@/app/stage/absStage';
import CitySelect from '../comps/playing/CitySelect.vue';
import InnerMain from '../comps/playing/InnerMain.vue';
import CityRes from '../comps/playing/CityRes.vue';
import CityBuilding from '../comps/playing/funpanl/CityBuilding.vue';


const { sg } = inject('sg') as { sg: SanGuo }

const stage = ref<Stage>()
watch(sg.ctx.stageMgr.stageKey, async (newStageKey, oldStageKey) => {
    console.log(`stageView: watch newStage: ${newStageKey},oldStage: ${oldStageKey}`)
    const oldStage = sg.ctx.stageMgr.getStage(oldStageKey)
    const newStage = sg.ctx.stageMgr.getStage(newStageKey)
    oldStage && await oldStage.onUnmounted()
    newStage && await newStage.onMounted()

    stage.value = newStage
}, { immediate: true, deep: true })

onMounted(() => { sg.ctx.stage })

onUnmounted(async () => {
    stage.value && await stage.value.onUnmounted()
})
</script>
<template>
    <div class="sg-playing">
        <template v-if="stage">
            <template v-for="comp in stage.cfgs" :key="comp.key()">
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
                    <CityRes v-if="comp.show() && comp.refComp() == 'cityRes'" :cfg="comp" :id="comp.key()" />
                    <CityBuilding v-if="comp.show() && comp.refComp() == 'buildList'" :cfg="comp" :id="comp.key()" />

                    
                    <!--                        -->


                    <!--  -->
                    <!-- <ChatPan v-if="comp.show() && comp.refComp() == 'chatPan'" :cfg="comp" :id="comp.key()" />
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