# 构建与组件整合

本公开仓库没有完整本地版入口、配置类和 HUD，因此不能直接执行 `gradle build` 得到完整 Mod。`components/original/` 保存明确新写的冲刺与披风组件；`components/mpl/` 保存 MPL-2.0 格挡文件。组件中对 LegacyPvp/Settings 的引用是原工程接口，需要在你的授权工程中连接，不是打包产物。

披风 UV 工具可独立使用 Java 21：

```powershell
java tools/PackCape.java artwork/input.png output/cape.png
```

`artwork/input.png` 需由你提供；只对拥有权利的图片执行打包。工具调用源文件模式，不生成本仓库的 Mod。
