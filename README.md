# Legacy PvP 组件

这里保留 Legacy PvP Assist 的切换冲刺、披风选择与同步、披风 UV 打包工具，以及 Fuzs 的 MPL-2.0 格挡文件。

原工程面向 Minecraft **1.21.1 / Fabric / Java 21**，版本 `1.0.7-mc1.21.1`，Mod ID：`legacy_pvp_assist`。

这是组件源码，**不能直接构建或安装为完整 Mod**。旧 HUD、视角和混合实现没有附带；整合组件还需要宿主的 `LegacyPvp`、`Settings` 等接口，格挡文件也有未附带的依赖。披风同步需连接对应服务端组件。切换冲刺不自动攻击或点击。

披风 UV 工具可以单独运行，方法见 [构建与整合](BUILDING.md)。输入图片需自行提供并有权使用。

[安装说明](INSTALLATION.md) · [来源与许可](THIRD_PARTY_NOTICES.md)

独立组件与工具采用 GPL-3.0-only；`HeldItemMixin.java` 保留 Fuzs 的 MPL-2.0 许可和署名。原工程参考 Fyu、spiderfrog、BigBFace、OrangeMarshall、prplz，未授权的旧代码和贴图不在仓库中。整理时未重新编译或进游戏测试。

## 贡献者

Demon-wxj · GPT（OpenAI）。分工见 [CONTRIBUTORS.md](CONTRIBUTORS.md)。
