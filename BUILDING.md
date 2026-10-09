# 构建与整合

仓库没有完整的入口、配置类和 HUD，不能直接用 `gradle build` 得到 Mod JAR。

- `components/original/`：切换冲刺和披风组件，采用 GPL-3.0-only。
- `components/mpl/`：Fuzs 的格挡文件，保持 MPL-2.0。

组件引用的 `LegacyPvp`、`Settings` 等宿主接口没有附带，需要在有权使用的工程里补齐。格挡文件的其他依赖也需自行提供。

披风 UV 工具可用 **Java 21** 独立运行：

```powershell
java tools/PackCape.java artwork/input.png output/cape.png
```

输入图片需自行提供并有权使用。这个命令只处理图片，不生成 Mod。
