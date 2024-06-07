<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<?mso-application progid="Word.Document"?>
<pkg:package xmlns:pkg="http://schemas.microsoft.com/office/2006/xmlPackage">
    <pkg:part pkg:name="/_rels/.rels" pkg:contentType="application/vnd.openxmlformats-package.relationships+xml">
        <pkg:xmlData>
            <Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
                <Relationship Id="rId4" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="word/document.xml"/>
                <Relationship Id="rId2" Type="http://schemas.openxmlformats.org/package/2006/relationships/metadata/core-properties" Target="docProps/core.xml"/>
                <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/extended-properties" Target="docProps/app.xml"/>
                <Relationship Id="rId3" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/custom-properties" Target="docProps/custom.xml"/>
            </Relationships>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/word/_rels/document.xml.rels"
              pkg:contentType="application/vnd.openxmlformats-package.relationships+xml">
        <pkg:xmlData>
            <Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
                    <#-- 本月和上月的日电量曲线 -->
                    <Relationship Id="rId4" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="media/image1.png"/>
                    <#-- 本月电量 -->
                    <Relationship Id="rId5" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="media/image2.png"/>
                    <#-- 本月电费 -->
                    <Relationship Id="rId6" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="media/image3.png"/>
                    <#-- 各厂区电量汇总 -->
                    <Relationship Id="rId7" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="media/image4.png"/>
                    <#-- 用电负荷 -->
                    <Relationship Id="rId8" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="media/image5.png"/>
                    <#-- 谐波电流 -->
                    <#list table3 as harPlan>
                        <Relationship Id="${harPlan.rId}" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="media/${harPlan.image}.png"/>
                    </#list>
                    <#-- 谐波电压 -->
                    <#list table4 as harVPlan>
                        <Relationship Id="${harVPlan.rId}" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="media/${harVPlan.image}.png"/>
                    </#list>
                    <#-- 企业设备能效图片 -->
                    <Relationship Id="${chartEERid}" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="media/${chartEEImage}.png"/>
                    <#list canUseParam as canUse>
                        <Relationship Id="${canUse.maxTimerId}" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="media/${canUse.maxTimeimage}.png"/>
                        <Relationship Id="${canUse.maxLoadrId}" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="media/${canUse.maxLoadimage}.png"/>
                        <Relationship Id="${canUse.maxIToprId}" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="media/${canUse.maxITopimage}.png"/>
                        <Relationship Id="${canUse.maxItimerId}" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="media/${canUse.maxItimeimage}.png"/>
                        <Relationship Id="${canUse.sumItimerId}" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="media/${canUse.sumItimeimage}.png"/>
                        <Relationship Id="${canUse.maxVtoprId}" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="media/${canUse.maxVtopimage}.png"/>
                        <Relationship Id="${canUse.maxVtimerId}" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="media/${canUse.maxVtimeimage}.png"/>
                        <Relationship Id="${canUse.sumVtimerId}" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="media/${canUse.sumVtimeimage}.png"/>
                    </#list>

                <Relationship Id="rId2" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/settings" Target="settings.xml"/>
                <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles" Target="styles.xml"/>
                <Relationship Id="rId3" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/theme" Target="theme/theme1.xml"/>
                <Relationship Id="rId20" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/fontTable" Target="fontTable.xml"/>
                <Relationship Id="rId19" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/customXml" Target="../customXml/item1.xml"/>
            </Relationships>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/word/document.xml"
              pkg:contentType="application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml">
        <pkg:xmlData>
            <w:document xmlns:wpc="http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas"
                        xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006"
                        xmlns:o="urn:schemas-microsoft-com:office:office"
                        xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships"
                        xmlns:m="http://schemas.openxmlformats.org/officeDocument/2006/math"
                        xmlns:v="urn:schemas-microsoft-com:vml"
                        xmlns:wp14="http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing"
                        xmlns:wp="http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing"
                        xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main"
                        xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml"
                        xmlns:w10="urn:schemas-microsoft-com:office:word"
                        xmlns:w15="http://schemas.microsoft.com/office/word/2012/wordml"
                        xmlns:wpg="http://schemas.microsoft.com/office/word/2010/wordprocessingGroup"
                        xmlns:wpi="http://schemas.microsoft.com/office/word/2010/wordprocessingInk"
                        xmlns:wne="http://schemas.microsoft.com/office/word/2006/wordml"
                        xmlns:wps="http://schemas.microsoft.com/office/word/2010/wordprocessingShape"
                        xmlns:wpsCustomData="http://www.wps.cn/officeDocument/2013/wpsCustomData"
                        mc:Ignorable="w14 w15 wp14">
                <w:body>
                    <w:p>
                        <w:pPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:b/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="32"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="32"/>
                            </w:rPr>
                            <w:t>${month}月服务报告</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                            <w:t>尊敬的${ledgerName}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                            <w:t>根据智慧能效云平台数据分析，本期您的用电情况如下：</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="13"/>
                                <w:szCs w:val="13"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="13"/>
                                <w:szCs w:val="13"/>
                            </w:rPr>
                            <w:t>本月和上月的日电量曲线</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:drawing>
                                <wp:inline distT="0" distB="0" distL="0" distR="0">
                                    <wp:extent cx="5274310" cy="1383030"/>
                                    <wp:effectExtent l="19050" t="0" r="2050" b="0"/>
                                    <wp:docPr id="1" name="图片 1"/>
                                    <wp:cNvGraphicFramePr>
                                        <a:graphicFrameLocks
                                                xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main"
                                                noChangeAspect="1"/>
                                    </wp:cNvGraphicFramePr>
                                    <a:graphic xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main">
                                        <a:graphicData uri="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                            <pic:pic
                                                    xmlns:pic="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                                <pic:nvPicPr>
                                                    <pic:cNvPr id="1" name="图片 1"/>
                                                    <pic:cNvPicPr>
                                                        <a:picLocks noChangeAspect="1" noChangeArrowheads="1"/>
                                                    </pic:cNvPicPr>
                                                </pic:nvPicPr>
                                                <pic:blipFill>
                                                    <a:blip r:embed="rId4"/>
                                                    <a:srcRect/>
                                                    <a:stretch>
                                                        <a:fillRect/>
                                                    </a:stretch>
                                                </pic:blipFill>
                                                <pic:spPr>
                                                    <a:xfrm>
                                                        <a:off x="0" y="0"/>
                                                        <a:ext cx="5274310" cy="1383347"/>
                                                    </a:xfrm>
                                                    <a:prstGeom prst="rect">
                                                        <a:avLst/>
                                                    </a:prstGeom>
                                                    <a:noFill/>
                                                    <a:ln w="9525">
                                                        <a:noFill/>
                                                        <a:miter lim="800000"/>
                                                        <a:headEnd/>
                                                        <a:tailEnd/>
                                                    </a:ln>
                                                </pic:spPr>
                                            </pic:pic>
                                        </a:graphicData>
                                    </a:graphic>
                                </wp:inline>
                            </w:drawing>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="13"/>
                                <w:szCs w:val="13"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="13"/>
                                <w:szCs w:val="13"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="13"/>
                                <w:szCs w:val="13"/>
                            </w:rPr>
                            <w:t>本月电量和电费</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="13"/>
                                <w:szCs w:val="13"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="13"/>
                                <w:szCs w:val="13"/>
                            </w:rPr>
                            <w:drawing>
                                <wp:inline distT="0" distB="0" distL="0" distR="0">
                                    <wp:extent cx="2545080" cy="1249045"/>
                                    <wp:effectExtent l="19050" t="0" r="7092" b="0"/>
                                    <wp:docPr id="4" name="图片 4"/>
                                    <wp:cNvGraphicFramePr>
                                        <a:graphicFrameLocks
                                                xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main"
                                                noChangeAspect="1"/>
                                    </wp:cNvGraphicFramePr>
                                    <a:graphic xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main">
                                        <a:graphicData uri="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                            <pic:pic
                                                    xmlns:pic="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                                <pic:nvPicPr>
                                                    <pic:cNvPr id="4" name="图片 4"/>
                                                    <pic:cNvPicPr>
                                                        <a:picLocks noChangeAspect="1" noChangeArrowheads="1"/>
                                                    </pic:cNvPicPr>
                                                </pic:nvPicPr>
                                                <pic:blipFill>
                                                    <a:blip r:embed="rId5"/>
                                                    <a:srcRect/>
                                                    <a:stretch>
                                                        <a:fillRect/>
                                                    </a:stretch>
                                                </pic:blipFill>
                                                <pic:spPr>
                                                    <a:xfrm>
                                                        <a:off x="0" y="0"/>
                                                        <a:ext cx="2545230" cy="1249488"/>
                                                    </a:xfrm>
                                                    <a:prstGeom prst="rect">
                                                        <a:avLst/>
                                                    </a:prstGeom>
                                                    <a:noFill/>
                                                    <a:ln w="9525">
                                                        <a:noFill/>
                                                        <a:miter lim="800000"/>
                                                        <a:headEnd/>
                                                        <a:tailEnd/>
                                                    </a:ln>
                                                </pic:spPr>
                                            </pic:pic>
                                        </a:graphicData>
                                    </a:graphic>
                                </wp:inline>
                            </w:drawing>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="13"/>
                                <w:szCs w:val="13"/>
                            </w:rPr>
                            <w:t xml:space="preserve"> </w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="13"/>
                                <w:szCs w:val="13"/>
                            </w:rPr>
                            <w:drawing>
                                <wp:inline distT="0" distB="0" distL="0" distR="0">
                                    <wp:extent cx="2545715" cy="1268730"/>
                                    <wp:effectExtent l="19050" t="0" r="6680" b="0"/>
                                    <wp:docPr id="7" name="图片 7"/>
                                    <wp:cNvGraphicFramePr>
                                        <a:graphicFrameLocks
                                                xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main"
                                                noChangeAspect="1"/>
                                    </wp:cNvGraphicFramePr>
                                    <a:graphic xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main">
                                        <a:graphicData uri="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                            <pic:pic
                                                    xmlns:pic="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                                <pic:nvPicPr>
                                                    <pic:cNvPr id="7" name="图片 7"/>
                                                    <pic:cNvPicPr>
                                                        <a:picLocks noChangeAspect="1" noChangeArrowheads="1"/>
                                                    </pic:cNvPicPr>
                                                </pic:nvPicPr>
                                                <pic:blipFill>
                                                    <a:blip r:embed="rId6"/>
                                                    <a:srcRect/>
                                                    <a:stretch>
                                                        <a:fillRect/>
                                                    </a:stretch>
                                                </pic:blipFill>
                                                <pic:spPr>
                                                    <a:xfrm>
                                                        <a:off x="0" y="0"/>
                                                        <a:ext cx="2545842" cy="1269127"/>
                                                    </a:xfrm>
                                                    <a:prstGeom prst="rect">
                                                        <a:avLst/>
                                                    </a:prstGeom>
                                                    <a:noFill/>
                                                    <a:ln w="9525">
                                                        <a:noFill/>
                                                        <a:miter lim="800000"/>
                                                        <a:headEnd/>
                                                        <a:tailEnd/>
                                                    </a:ln>
                                                </pic:spPr>
                                            </pic:pic>
                                        </a:graphicData>
                                    </a:graphic>
                                </wp:inline>
                            </w:drawing>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                            <w:t>电量：</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="00B050"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>${allEle}</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                            <w:t xml:space="preserve"> kWh</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                            <w:t>电费：</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="00B050"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>${allFee}</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                            <w:t>元</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                            <w:t>月总共用电：</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>${eleTotal}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                            <w:t>尖端电量占：</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>${ele1}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                            <w:t>峰段电量占：</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>${ele2}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                            <w:t>平段电量占：</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>${ele3}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                            <w:t>谷端电量占：</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>${ele4}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                            <w:t>电费申报方式：</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>${demandType}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                            <w:t>月最大需量：</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>${maxMD}</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="00B050"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t xml:space="preserve"> </w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                            <w:t>kWh</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                            <w:t>月平均功率因数：</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>${avgPF}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                            <w:t>变压器容量：</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t xml:space="preserve">${vol} </w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                            <w:t>KVA</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t xml:space="preserve"> </w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                            <w:t>电网申报量：</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t xml:space="preserve">${declare} </w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                            <w:t>KVA</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="13"/>
                                <w:szCs w:val="13"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="13"/>
                                <w:szCs w:val="13"/>
                            </w:rPr>
                            <w:t>各厂区用能汇总</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="13"/>
                                <w:szCs w:val="13"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="13"/>
                                <w:szCs w:val="13"/>
                            </w:rPr>
                            <w:drawing>
                                <wp:inline distT="0" distB="0" distL="0" distR="0">
                                    <wp:extent cx="2547620" cy="1673860"/>
                                    <wp:effectExtent l="19050" t="0" r="4518" b="0"/>
                                    <wp:docPr id="3" name="图片 1"/>
                                    <wp:cNvGraphicFramePr>
                                        <a:graphicFrameLocks
                                                xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main"
                                                noChangeAspect="1"/>
                                    </wp:cNvGraphicFramePr>
                                    <a:graphic xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main">
                                        <a:graphicData uri="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                            <pic:pic
                                                    xmlns:pic="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                                <pic:nvPicPr>
                                                    <pic:cNvPr id="3" name="图片 1"/>
                                                    <pic:cNvPicPr>
                                                        <a:picLocks noChangeAspect="1" noChangeArrowheads="1"/>
                                                    </pic:cNvPicPr>
                                                </pic:nvPicPr>
                                                <pic:blipFill>
                                                    <a:blip r:embed="rId7"/>
                                                    <a:srcRect/>
                                                    <a:stretch>
                                                        <a:fillRect/>
                                                    </a:stretch>
                                                </pic:blipFill>
                                                <pic:spPr>
                                                    <a:xfrm>
                                                        <a:off x="0" y="0"/>
                                                        <a:ext cx="2552781" cy="1677443"/>
                                                    </a:xfrm>
                                                    <a:prstGeom prst="rect">
                                                        <a:avLst/>
                                                    </a:prstGeom>
                                                    <a:noFill/>
                                                    <a:ln w="9525">
                                                        <a:noFill/>
                                                        <a:miter lim="800000"/>
                                                        <a:headEnd/>
                                                        <a:tailEnd/>
                                                    </a:ln>
                                                </pic:spPr>
                                            </pic:pic>
                                        </a:graphicData>
                                    </a:graphic>
                                </wp:inline>
                            </w:drawing>
                        </w:r>
                    </w:p>
                    <w:tbl>
                        <w:tblPr>
                            <w:tblStyle w:val="8"/>
                            <w:tblW w:w="0" w:type="auto"/>
                            <w:tblInd w:w="-967" w:type="dxa"/>
                            <w:tblBorders>
                                <w:top w:val="single" w:color="000000" w:themeColor="text1" w:sz="4" w:space="0"/>
                                <w:left w:val="single" w:color="000000" w:themeColor="text1" w:sz="4" w:space="0"/>
                                <w:bottom w:val="single" w:color="000000" w:themeColor="text1" w:sz="4" w:space="0"/>
                                <w:right w:val="single" w:color="000000" w:themeColor="text1" w:sz="4" w:space="0"/>
                                <w:insideH w:val="single" w:color="000000" w:themeColor="text1" w:sz="4" w:space="0"/>
                                <w:insideV w:val="single" w:color="000000" w:themeColor="text1" w:sz="4" w:space="0"/>
                            </w:tblBorders>
                            <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                   w:themeFillTint="99"/>
                            <w:tblLayout w:type="autofit"/>
                            <w:tblCellMar>
                                <w:top w:w="0" w:type="dxa"/>
                                <w:left w:w="108" w:type="dxa"/>
                                <w:bottom w:w="0" w:type="dxa"/>
                                <w:right w:w="108" w:type="dxa"/>
                            </w:tblCellMar>
                        </w:tblPr>
                        <w:tblGrid>
                            <w:gridCol w:w="3176"/>
                            <w:gridCol w:w="3265"/>
                            <w:gridCol w:w="3048"/>
                        </w:tblGrid>
                        <w:tr>
                            <w:tblPrEx>
                                <w:tblBorders>
                                    <w:top w:val="single" w:color="000000" w:themeColor="text1" w:sz="4" w:space="0"/>
                                    <w:left w:val="single" w:color="000000" w:themeColor="text1" w:sz="4" w:space="0"/>
                                    <w:bottom w:val="single" w:color="000000" w:themeColor="text1" w:sz="4"
                                              w:space="0"/>
                                    <w:right w:val="single" w:color="000000" w:themeColor="text1" w:sz="4" w:space="0"/>
                                    <w:insideH w:val="single" w:color="000000" w:themeColor="text1" w:sz="4"
                                               w:space="0"/>
                                    <w:insideV w:val="single" w:color="000000" w:themeColor="text1" w:sz="4"
                                               w:space="0"/>
                                </w:tblBorders>
                                <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                       w:themeFillTint="99"/>
                                <w:tblCellMar>
                                    <w:top w:w="0" w:type="dxa"/>
                                    <w:left w:w="108" w:type="dxa"/>
                                    <w:bottom w:w="0" w:type="dxa"/>
                                    <w:right w:w="108" w:type="dxa"/>
                                </w:tblCellMar>
                            </w:tblPrEx>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3176" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7E7E7E" w:themeFill="text1"
                                           w:themeFillTint="80"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:b/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                        </w:rPr>
                                        <w:t>名称</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3265" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7E7E7E" w:themeFill="text1"
                                           w:themeFillTint="80"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                        </w:rPr>
                                        <w:t>用电量(kWh)</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3048" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7E7E7E" w:themeFill="text1"
                                           w:themeFillTint="80"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                        </w:rPr>
                                        <w:t>占比(%)</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        <#list table1 as plan1>
                        <w:tr>
                            <w:tblPrEx>
                                <w:tblBorders>
                                    <w:top w:val="single" w:color="000000" w:themeColor="text1" w:sz="4" w:space="0"/>
                                    <w:left w:val="single" w:color="000000" w:themeColor="text1" w:sz="4" w:space="0"/>
                                    <w:bottom w:val="single" w:color="000000" w:themeColor="text1" w:sz="4"
                                              w:space="0"/>
                                    <w:right w:val="single" w:color="000000" w:themeColor="text1" w:sz="4" w:space="0"/>
                                    <w:insideH w:val="single" w:color="000000" w:themeColor="text1" w:sz="4"
                                               w:space="0"/>
                                    <w:insideV w:val="single" w:color="000000" w:themeColor="text1" w:sz="4"
                                               w:space="0"/>
                                </w:tblBorders>
                                <w:tblCellMar>
                                    <w:top w:w="0" w:type="dxa"/>
                                    <w:left w:w="108" w:type="dxa"/>
                                    <w:bottom w:w="0" w:type="dxa"/>
                                    <w:right w:w="108" w:type="dxa"/>
                                </w:tblCellMar>
                            </w:tblPrEx>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3176" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                        </w:rPr>
                                        <w:t>${plan1.name}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3265" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="8DB3E2" w:themeFill="text2"
                                           w:themeFillTint="66"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                        </w:rPr>
                                        <w:t>${plan1.Q}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3048" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                        </w:rPr>
                                        <w:t>${plan1.Qpercent}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        
                        </#list>
                            
                    </w:tbl>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                            <w:t>最大负载率采集点TOP3：</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>${maxLoad}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                            <w:t>最小功率因数采集点TOP3：</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>${minPF}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                            <w:t>电压不平衡日均采集点TOP3：</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>${VBalanAvg}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                            <w:t>电流不平衡度日均采集点TOP3：</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>${IBalanAvg}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:eastAsiaTheme="minorEastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="13"/>
                                <w:szCs w:val="13"/>
                                <w:lang w:eastAsia="zh-CN"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="13"/>
                                <w:szCs w:val="13"/>
                                <w:lang w:eastAsia="zh-CN"/>
                            </w:rPr>
                            <w:t>用电负荷</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:drawing>
                                <wp:inline distT="0" distB="0" distL="114300" distR="114300">
                                    <wp:extent cx="6049645" cy="2047875"/>
                                    <wp:effectExtent l="0" t="0" r="8255" b="9525"/>
                                    <wp:docPr id="5" name="图片 1"/>
                                    <wp:cNvGraphicFramePr>
                                        <a:graphicFrameLocks
                                                xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main"
                                                noChangeAspect="1"/>
                                    </wp:cNvGraphicFramePr>
                                    <a:graphic xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main">
                                        <a:graphicData uri="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                            <pic:pic
                                                    xmlns:pic="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                                <pic:nvPicPr>
                                                    <pic:cNvPr id="5" name="图片 1"/>
                                                    <pic:cNvPicPr>
                                                        <a:picLocks noChangeAspect="1"/>
                                                    </pic:cNvPicPr>
                                                </pic:nvPicPr>
                                                <pic:blipFill>
                                                    <a:blip r:embed="rId8"/>
                                                    <a:stretch>
                                                        <a:fillRect/>
                                                    </a:stretch>
                                                </pic:blipFill>
                                                <pic:spPr>
                                                    <a:xfrm>
                                                        <a:off x="0" y="0"/>
                                                        <a:ext cx="6049645" cy="2047875"/>
                                                    </a:xfrm>
                                                    <a:prstGeom prst="rect">
                                                        <a:avLst/>
                                                    </a:prstGeom>
                                                    <a:noFill/>
                                                    <a:ln>
                                                        <a:noFill/>
                                                    </a:ln>
                                                </pic:spPr>
                                            </pic:pic>
                                        </a:graphicData>
                                    </a:graphic>
                                </wp:inline>
                            </w:drawing>
                        </w:r>
                    </w:p>
                    <w:tbl>
                        <w:tblPr>
                            <w:tblStyle w:val="8"/>
                            <w:tblW w:w="10884" w:type="dxa"/>
                            <w:tblInd w:w="-976" w:type="dxa"/>
                            <w:tblBorders>
                                <w:top w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                <w:left w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                <w:bottom w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                <w:right w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                <w:insideH w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                <w:insideV w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                            </w:tblBorders>
                            <w:tblLayout w:type="fixed"/>
                            <w:tblCellMar>
                                <w:top w:w="0" w:type="dxa"/>
                                <w:left w:w="108" w:type="dxa"/>
                                <w:bottom w:w="0" w:type="dxa"/>
                                <w:right w:w="108" w:type="dxa"/>
                            </w:tblCellMar>
                        </w:tblPr>
                        <w:tblGrid>
                            <w:gridCol w:w="1002"/>
                            <w:gridCol w:w="406"/>
                            <w:gridCol w:w="609"/>
                            <w:gridCol w:w="860"/>
                            <w:gridCol w:w="754"/>
                            <w:gridCol w:w="786"/>
                            <w:gridCol w:w="670"/>
                            <w:gridCol w:w="706"/>
                            <w:gridCol w:w="724"/>
                            <w:gridCol w:w="697"/>
                            <w:gridCol w:w="635"/>
                            <w:gridCol w:w="750"/>
                            <w:gridCol w:w="750"/>
                            <w:gridCol w:w="777"/>
                            <w:gridCol w:w="758"/>
                        </w:tblGrid>
                        <w:tr>
                            <w:tblPrEx>
                                <w:tblBorders>
                                    <w:top w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:left w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:bottom w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:right w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideH w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideV w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                </w:tblBorders>
                                <w:tblCellMar>
                                    <w:top w:w="0" w:type="dxa"/>
                                    <w:left w:w="108" w:type="dxa"/>
                                    <w:bottom w:w="0" w:type="dxa"/>
                                    <w:right w:w="108" w:type="dxa"/>
                                </w:tblCellMar>
                            </w:tblPrEx>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2017" w:type="dxa"/>
                                    <w:gridSpan w:val="3"/>
                                    <w:vMerge w:val="restart"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:bCs/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:bCs/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>日期</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="8867" w:type="dxa"/>
                                    <w:gridSpan w:val="12"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:b/>
                                            <w:bCs/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:bCs/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>有功功率</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:bCs/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>(kW)</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        <w:tr>
                            <w:tblPrEx>
                                <w:tblBorders>
                                    <w:top w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:left w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:bottom w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:right w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideH w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideV w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                </w:tblBorders>
                                <w:tblCellMar>
                                    <w:top w:w="0" w:type="dxa"/>
                                    <w:left w:w="108" w:type="dxa"/>
                                    <w:bottom w:w="0" w:type="dxa"/>
                                    <w:right w:w="108" w:type="dxa"/>
                                </w:tblCellMar>
                            </w:tblPrEx>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2017" w:type="dxa"/>
                                    <w:gridSpan w:val="3"/>
                                    <w:vMerge w:val="continue"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:bCs/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="860" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:asciiTheme="minorHAnsi"
                                                      w:hAnsiTheme="minorHAnsi" w:eastAsiaTheme="minorEastAsia"
                                                      w:cstheme="minorBidi"/>
                                            <w:b/>
                                            <w:bCs/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:bCs/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>0:00(12)</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="754" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:asciiTheme="minorHAnsi"
                                                      w:hAnsiTheme="minorHAnsi" w:eastAsiaTheme="minorEastAsia"
                                                      w:cstheme="minorBidi"/>
                                            <w:b/>
                                            <w:bCs/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:bCs/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>1:00</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="786" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:asciiTheme="minorHAnsi"
                                                      w:hAnsiTheme="minorHAnsi" w:eastAsiaTheme="minorEastAsia"
                                                      w:cstheme="minorBidi"/>
                                            <w:b/>
                                            <w:bCs/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:bCs/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>2:00</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="670" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:asciiTheme="minorHAnsi"
                                                      w:hAnsiTheme="minorHAnsi" w:eastAsiaTheme="minorEastAsia"
                                                      w:cstheme="minorBidi"/>
                                            <w:b/>
                                            <w:bCs/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:bCs/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>3:00</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="706" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:asciiTheme="minorHAnsi"
                                                      w:hAnsiTheme="minorHAnsi" w:eastAsiaTheme="minorEastAsia"
                                                      w:cstheme="minorBidi"/>
                                            <w:b/>
                                            <w:bCs/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:bCs/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>4:00</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="724" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:asciiTheme="minorHAnsi"
                                                      w:hAnsiTheme="minorHAnsi" w:eastAsiaTheme="minorEastAsia"
                                                      w:cstheme="minorBidi"/>
                                            <w:b/>
                                            <w:bCs/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:bCs/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>5:00</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="697" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:asciiTheme="minorHAnsi"
                                                      w:hAnsiTheme="minorHAnsi" w:eastAsiaTheme="minorEastAsia"
                                                      w:cstheme="minorBidi"/>
                                            <w:b/>
                                            <w:bCs/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:bCs/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>6:00</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="635" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:asciiTheme="minorHAnsi"
                                                      w:hAnsiTheme="minorHAnsi" w:eastAsiaTheme="minorEastAsia"
                                                      w:cstheme="minorBidi"/>
                                            <w:b/>
                                            <w:bCs/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:bCs/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>7:00</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="750" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:asciiTheme="minorHAnsi"
                                                      w:hAnsiTheme="minorHAnsi" w:eastAsiaTheme="minorEastAsia"
                                                      w:cstheme="minorBidi"/>
                                            <w:b/>
                                            <w:bCs/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:bCs/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>8:00</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="750" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:asciiTheme="minorHAnsi"
                                                      w:hAnsiTheme="minorHAnsi" w:eastAsiaTheme="minorEastAsia"
                                                      w:cstheme="minorBidi"/>
                                            <w:b/>
                                            <w:bCs/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:bCs/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>9:00</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="777" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:asciiTheme="minorHAnsi"
                                                      w:hAnsiTheme="minorHAnsi" w:eastAsiaTheme="minorEastAsia"
                                                      w:cstheme="minorBidi"/>
                                            <w:b/>
                                            <w:bCs/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:bCs/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>10:00</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="758" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:asciiTheme="minorHAnsi"
                                                      w:hAnsiTheme="minorHAnsi" w:eastAsiaTheme="minorEastAsia"
                                                      w:cstheme="minorBidi"/>
                                            <w:b/>
                                            <w:bCs/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:bCs/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>11:00</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        <#list table2 as powerList>
                        <w:tr>
                            <w:tblPrEx>
                                <w:tblBorders>
                                    <w:top w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:left w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:bottom w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:right w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideH w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideV w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                </w:tblBorders>
                                <w:tblCellMar>
                                    <w:top w:w="0" w:type="dxa"/>
                                    <w:left w:w="108" w:type="dxa"/>
                                    <w:bottom w:w="0" w:type="dxa"/>
                                    <w:right w:w="108" w:type="dxa"/>
                                </w:tblCellMar>
                            </w:tblPrEx>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1002" w:type="dxa"/>
                                    <w:vMerge w:val="restart"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${powerList.dataTime}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="406" w:type="dxa"/>
                                    <w:vMerge w:val="restart"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="8DB3E2" w:themeFill="text2"
                                           w:themeFillTint="66"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>总</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="609" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>AM</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="860" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="8DB3E2" w:themeFill="text2"
                                           w:themeFillTint="66"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${powerList.kW0}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="754" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${powerList.kW1}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="786" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="8DB3E2" w:themeFill="text2"
                                           w:themeFillTint="66"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${powerList.kW2}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="670" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${powerList.kW3}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="706" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="8DB3E2" w:themeFill="text2"
                                           w:themeFillTint="66"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${powerList.kW4}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="724" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${powerList.kW5}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="697" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="8DB3E2" w:themeFill="text2"
                                           w:themeFillTint="66"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${powerList.kW6}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="635" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${powerList.kW7}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="750" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="8DB3E2" w:themeFill="text2"
                                           w:themeFillTint="66"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${powerList.kW8}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="750" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${powerList.kW9}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="777" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="8DB3E2" w:themeFill="text2"
                                           w:themeFillTint="66"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${powerList.kW10}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="758" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${powerList.kW11}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        <w:tr>
                            <w:tblPrEx>
                                <w:tblBorders>
                                    <w:top w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:left w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:bottom w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:right w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideH w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideV w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                </w:tblBorders>
                                <w:tblCellMar>
                                    <w:top w:w="0" w:type="dxa"/>
                                    <w:left w:w="108" w:type="dxa"/>
                                    <w:bottom w:w="0" w:type="dxa"/>
                                    <w:right w:w="108" w:type="dxa"/>
                                </w:tblCellMar>
                            </w:tblPrEx>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1002" w:type="dxa"/>
                                    <w:vMerge w:val="continue"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="406" w:type="dxa"/>
                                    <w:vMerge w:val="continue"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="8DB3E2" w:themeFill="text2"
                                           w:themeFillTint="66"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="609" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>PM</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="860" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="8DB3E2" w:themeFill="text2"
                                           w:themeFillTint="66"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${powerList.kW12}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="754" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${powerList.kW13}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="786" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="8DB3E2" w:themeFill="text2"
                                           w:themeFillTint="66"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${powerList.kW14}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="670" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${powerList.kW15}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="706" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="8DB3E2" w:themeFill="text2"
                                           w:themeFillTint="66"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${powerList.kW16}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="724" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${powerList.kW17}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="697" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="8DB3E2" w:themeFill="text2"
                                           w:themeFillTint="66"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${powerList.kW18}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="635" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${powerList.kW19}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="750" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="8DB3E2" w:themeFill="text2"
                                           w:themeFillTint="66"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${powerList.kW20}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="750" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${powerList.kW21}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="777" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="8DB3E2" w:themeFill="text2"
                                           w:themeFillTint="66"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${powerList.kW22}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="758" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${powerList.kW23}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        </#list>
                    </w:tbl>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:hint="default" w:eastAsiaTheme="minorEastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="13"/>
                                <w:szCs w:val="13"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="13"/>
                                <w:szCs w:val="13"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                            </w:rPr>
                            <w:t>谐波情况</w:t>
                        </w:r>
                    </w:p>
                    <#list table3 as harPlan>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="微软雅黑" w:hAnsi="微软雅黑" w:eastAsia="微软雅黑" w:cs="微软雅黑"/>
                                <w:i w:val="0"/>
                                <w:caps w:val="0"/>
                                <w:color w:val="4B4B4B"/>
                                <w:spacing w:val="0"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                                <w:shd w:val="clear" w:fill="FFFFFF"/>
                            </w:rPr>
                            <w:t>${harPlan.harText}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:drawing>
                                <wp:inline distT="0" distB="0" distL="114300" distR="114300">
                                    <wp:extent cx="5269230" cy="1397635"/>
                                    <wp:effectExtent l="0" t="0" r="7620" b="12065"/>
                                    <wp:docPr id="6" name="图片 2"/>
                                    <wp:cNvGraphicFramePr>
                                        <a:graphicFrameLocks
                                                xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main"
                                                noChangeAspect="1"/>
                                    </wp:cNvGraphicFramePr>
                                    <a:graphic xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main">
                                        <a:graphicData uri="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                            <pic:pic
                                                    xmlns:pic="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                                <pic:nvPicPr>
                                                    <pic:cNvPr id="6" name="图片 2"/>
                                                    <pic:cNvPicPr>
                                                        <a:picLocks noChangeAspect="1"/>
                                                    </pic:cNvPicPr>
                                                </pic:nvPicPr>
                                                <pic:blipFill>
                                                    <a:blip r:embed="${harPlan.rId}"/>
                                                    <a:stretch>
                                                        <a:fillRect/>
                                                    </a:stretch>
                                                </pic:blipFill>
                                                <pic:spPr>
                                                    <a:xfrm>
                                                        <a:off x="0" y="0"/>
                                                        <a:ext cx="5269230" cy="1397635"/>
                                                    </a:xfrm>
                                                    <a:prstGeom prst="rect">
                                                        <a:avLst/>
                                                    </a:prstGeom>
                                                    <a:noFill/>
                                                    <a:ln>
                                                        <a:noFill/>
                                                    </a:ln>
                                                </pic:spPr>
                                            </pic:pic>
                                        </a:graphicData>
                                    </a:graphic>
                                </wp:inline>
                            </w:drawing>
                        </w:r>
                    </w:p>
                    <w:tbl>
                        <w:tblPr>
                            <w:tblStyle w:val="8"/>
                            <w:tblW w:w="0" w:type="auto"/>
                            <w:tblInd w:w="-976" w:type="dxa"/>
                            <w:tblBorders>
                                <w:top w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                <w:left w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                <w:bottom w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                <w:right w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                <w:insideH w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                <w:insideV w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                            </w:tblBorders>
                            <w:tblLayout w:type="autofit"/>
                            <w:tblCellMar>
                                <w:top w:w="0" w:type="dxa"/>
                                <w:left w:w="108" w:type="dxa"/>
                                <w:bottom w:w="0" w:type="dxa"/>
                                <w:right w:w="108" w:type="dxa"/>
                            </w:tblCellMar>
                        </w:tblPr>
                        <w:tblGrid>
                            <w:gridCol w:w="918"/>
                            <w:gridCol w:w="1217"/>
                            <w:gridCol w:w="1589"/>
                            <w:gridCol w:w="1085"/>
                            <w:gridCol w:w="1544"/>
                            <w:gridCol w:w="1068"/>
                            <w:gridCol w:w="2077"/>
                        </w:tblGrid>
                        <w:tr>
                            <w:tblPrEx>
                                <w:tblBorders>
                                    <w:top w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:left w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:bottom w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:right w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideH w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideV w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                </w:tblBorders>
                                <w:tblCellMar>
                                    <w:top w:w="0" w:type="dxa"/>
                                    <w:left w:w="108" w:type="dxa"/>
                                    <w:bottom w:w="0" w:type="dxa"/>
                                    <w:right w:w="108" w:type="dxa"/>
                                </w:tblCellMar>
                            </w:tblPrEx>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="918" w:type="dxa"/>
                                    <w:vMerge w:val="restart"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>谐波次数</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2806" w:type="dxa"/>
                                    <w:gridSpan w:val="2"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>A相谐波电流</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2629" w:type="dxa"/>
                                    <w:gridSpan w:val="2"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>B相谐波电流</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3145" w:type="dxa"/>
                                    <w:gridSpan w:val="2"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>C相谐波电流</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        <w:tr>
                            <w:tblPrEx>
                                <w:tblBorders>
                                    <w:top w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:left w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:bottom w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:right w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideH w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideV w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                </w:tblBorders>
                                <w:tblCellMar>
                                    <w:top w:w="0" w:type="dxa"/>
                                    <w:left w:w="108" w:type="dxa"/>
                                    <w:bottom w:w="0" w:type="dxa"/>
                                    <w:right w:w="108" w:type="dxa"/>
                                </w:tblCellMar>
                            </w:tblPrEx>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="918" w:type="dxa"/>
                                    <w:vMerge w:val="continue"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1217" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>日最大值</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>(A)</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1589" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>发生时间</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1085" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:asciiTheme="minorHAnsi"
                                                      w:hAnsiTheme="minorHAnsi" w:eastAsiaTheme="minorEastAsia"
                                                      w:cstheme="minorBidi"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>日最大值</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>(A)</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1544" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:asciiTheme="minorHAnsi"
                                                      w:hAnsiTheme="minorHAnsi" w:eastAsiaTheme="minorEastAsia"
                                                      w:cstheme="minorBidi"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>发生时间</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1068" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:asciiTheme="minorHAnsi"
                                                      w:hAnsiTheme="minorHAnsi" w:eastAsiaTheme="minorEastAsia"
                                                      w:cstheme="minorBidi"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>日最大值</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>(A)</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2077" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:asciiTheme="minorHAnsi"
                                                      w:hAnsiTheme="minorHAnsi" w:eastAsiaTheme="minorEastAsia"
                                                      w:cstheme="minorBidi"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>发生时间</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        <#list harPlan.harList as harList>
                        <w:tr>
                            <w:tblPrEx>
                                <w:tblBorders>
                                    <w:top w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:left w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:bottom w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:right w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideH w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideV w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                </w:tblBorders>
                                <w:tblCellMar>
                                    <w:top w:w="0" w:type="dxa"/>
                                    <w:left w:w="108" w:type="dxa"/>
                                    <w:bottom w:w="0" w:type="dxa"/>
                                    <w:right w:w="108" w:type="dxa"/>
                                </w:tblCellMar>
                            </w:tblPrEx>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="918" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${harList.num}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1217" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="8DB3E2" w:themeFill="text2"
                                           w:themeFillTint="66"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${harList.a_max}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1589" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${harList.a_time}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1085" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="8DB3E2" w:themeFill="text2"
                                           w:themeFillTint="66"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:asciiTheme="minorHAnsi"
                                                      w:hAnsiTheme="minorHAnsi" w:eastAsiaTheme="minorEastAsia"
                                                      w:cstheme="minorBidi"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${harList.b_max}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1544" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:asciiTheme="minorHAnsi"
                                                      w:hAnsiTheme="minorHAnsi" w:eastAsiaTheme="minorEastAsia"
                                                      w:cstheme="minorBidi"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${harList.b_time}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1068" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="8DB3E2" w:themeFill="text2"
                                           w:themeFillTint="66"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:asciiTheme="minorHAnsi"
                                                      w:hAnsiTheme="minorHAnsi" w:eastAsiaTheme="minorEastAsia"
                                                      w:cstheme="minorBidi"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${harList.c_max}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2077" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:asciiTheme="minorHAnsi"
                                                      w:hAnsiTheme="minorHAnsi" w:eastAsiaTheme="minorEastAsia"
                                                      w:cstheme="minorBidi"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${harList.c_time}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        </#list>
                    </w:tbl>
                    </#list>
                    <#list table4 as harVPlan>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="微软雅黑" w:hAnsi="微软雅黑" w:eastAsia="微软雅黑" w:cs="微软雅黑"/>
                                <w:i w:val="0"/>
                                <w:caps w:val="0"/>
                                <w:color w:val="4B4B4B"/>
                                <w:spacing w:val="0"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                                <w:shd w:val="clear" w:fill="FFFFFF"/>
                            </w:rPr>
                            <w:t>${harVPlan.harText}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:drawing>
                                <wp:inline distT="0" distB="0" distL="114300" distR="114300">
                                    <wp:extent cx="5269230" cy="1397635"/>
                                    <wp:effectExtent l="0" t="0" r="7620" b="12065"/>
                                    <wp:docPr id="8" name="图片 2"/>
                                    <wp:cNvGraphicFramePr>
                                        <a:graphicFrameLocks
                                                xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main"
                                                noChangeAspect="1"/>
                                    </wp:cNvGraphicFramePr>
                                    <a:graphic xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main">
                                        <a:graphicData uri="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                            <pic:pic
                                                    xmlns:pic="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                                <pic:nvPicPr>
                                                    <pic:cNvPr id="8" name="图片 2"/>
                                                    <pic:cNvPicPr>
                                                        <a:picLocks noChangeAspect="1"/>
                                                    </pic:cNvPicPr>
                                                </pic:nvPicPr>
                                                <pic:blipFill>
                                                    <a:blip r:embed="${harVPlan.rId}"/>
                                                    <a:stretch>
                                                        <a:fillRect/>
                                                    </a:stretch>
                                                </pic:blipFill>
                                                <pic:spPr>
                                                    <a:xfrm>
                                                        <a:off x="0" y="0"/>
                                                        <a:ext cx="5269230" cy="1397635"/>
                                                    </a:xfrm>
                                                    <a:prstGeom prst="rect">
                                                        <a:avLst/>
                                                    </a:prstGeom>
                                                    <a:noFill/>
                                                    <a:ln>
                                                        <a:noFill/>
                                                    </a:ln>
                                                </pic:spPr>
                                            </pic:pic>
                                        </a:graphicData>
                                    </a:graphic>
                                </wp:inline>
                            </w:drawing>
                        </w:r>
                    </w:p>
                    <w:tbl>
                        <w:tblPr>
                            <w:tblStyle w:val="8"/>
                            <w:tblW w:w="0" w:type="auto"/>
                            <w:tblInd w:w="-976" w:type="dxa"/>
                            <w:tblBorders>
                                <w:top w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                <w:left w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                <w:bottom w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                <w:right w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                <w:insideH w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                <w:insideV w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                            </w:tblBorders>
                            <w:tblLayout w:type="autofit"/>
                            <w:tblCellMar>
                                <w:top w:w="0" w:type="dxa"/>
                                <w:left w:w="108" w:type="dxa"/>
                                <w:bottom w:w="0" w:type="dxa"/>
                                <w:right w:w="108" w:type="dxa"/>
                            </w:tblCellMar>
                        </w:tblPr>
                        <w:tblGrid>
                            <w:gridCol w:w="918"/>
                            <w:gridCol w:w="1217"/>
                            <w:gridCol w:w="1589"/>
                            <w:gridCol w:w="1085"/>
                            <w:gridCol w:w="1544"/>
                            <w:gridCol w:w="1068"/>
                            <w:gridCol w:w="2077"/>
                        </w:tblGrid>
                        <w:tr>
                            <w:tblPrEx>
                                <w:tblBorders>
                                    <w:top w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:left w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:bottom w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:right w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideH w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideV w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                </w:tblBorders>
                                <w:tblCellMar>
                                    <w:top w:w="0" w:type="dxa"/>
                                    <w:left w:w="108" w:type="dxa"/>
                                    <w:bottom w:w="0" w:type="dxa"/>
                                    <w:right w:w="108" w:type="dxa"/>
                                </w:tblCellMar>
                            </w:tblPrEx>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="918" w:type="dxa"/>
                                    <w:vMerge w:val="restart"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>谐波次数</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2806" w:type="dxa"/>
                                    <w:gridSpan w:val="2"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>A相谐波电压</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2629" w:type="dxa"/>
                                    <w:gridSpan w:val="2"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>B相谐波电压</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3145" w:type="dxa"/>
                                    <w:gridSpan w:val="2"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>C相谐波电压</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        <w:tr>
                            <w:tblPrEx>
                                <w:tblBorders>
                                    <w:top w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:left w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:bottom w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:right w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideH w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideV w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                </w:tblBorders>
                                <w:tblCellMar>
                                    <w:top w:w="0" w:type="dxa"/>
                                    <w:left w:w="108" w:type="dxa"/>
                                    <w:bottom w:w="0" w:type="dxa"/>
                                    <w:right w:w="108" w:type="dxa"/>
                                </w:tblCellMar>
                            </w:tblPrEx>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="918" w:type="dxa"/>
                                    <w:vMerge w:val="continue"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1217" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>日最大值</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>(A)</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1589" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>发生时间</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1085" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:asciiTheme="minorHAnsi"
                                                      w:hAnsiTheme="minorHAnsi" w:eastAsiaTheme="minorEastAsia"
                                                      w:cstheme="minorBidi"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>日最大值</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>(A)</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1544" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:asciiTheme="minorHAnsi"
                                                      w:hAnsiTheme="minorHAnsi" w:eastAsiaTheme="minorEastAsia"
                                                      w:cstheme="minorBidi"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>发生时间</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1068" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:asciiTheme="minorHAnsi"
                                                      w:hAnsiTheme="minorHAnsi" w:eastAsiaTheme="minorEastAsia"
                                                      w:cstheme="minorBidi"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>日最大值</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>(A)</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2077" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:asciiTheme="minorHAnsi"
                                                      w:hAnsiTheme="minorHAnsi" w:eastAsiaTheme="minorEastAsia"
                                                      w:cstheme="minorBidi"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>发生时间</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        <#list harVPlan.harList as harList>
                        <w:tr>
                            <w:tblPrEx>
                                <w:tblBorders>
                                    <w:top w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:left w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:bottom w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:right w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideH w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideV w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                </w:tblBorders>
                                <w:tblCellMar>
                                    <w:top w:w="0" w:type="dxa"/>
                                    <w:left w:w="108" w:type="dxa"/>
                                    <w:bottom w:w="0" w:type="dxa"/>
                                    <w:right w:w="108" w:type="dxa"/>
                                </w:tblCellMar>
                            </w:tblPrEx>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="918" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${harList.num}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1217" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="8DB3E2" w:themeFill="text2"
                                           w:themeFillTint="66"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${harList.a_max}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1589" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${harList.a_time}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1085" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="8DB3E2" w:themeFill="text2"
                                           w:themeFillTint="66"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:asciiTheme="minorHAnsi"
                                                      w:hAnsiTheme="minorHAnsi" w:eastAsiaTheme="minorEastAsia"
                                                      w:cstheme="minorBidi"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${harList.b_max}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1544" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:asciiTheme="minorHAnsi"
                                                      w:hAnsiTheme="minorHAnsi" w:eastAsiaTheme="minorEastAsia"
                                                      w:cstheme="minorBidi"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${harList.b_time}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1068" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="8DB3E2" w:themeFill="text2"
                                           w:themeFillTint="66"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:asciiTheme="minorHAnsi"
                                                      w:hAnsiTheme="minorHAnsi" w:eastAsiaTheme="minorEastAsia"
                                                      w:cstheme="minorBidi"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${harList.c_max}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2077" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:asciiTheme="minorHAnsi"
                                                      w:hAnsiTheme="minorHAnsi" w:eastAsiaTheme="minorEastAsia"
                                                      w:cstheme="minorBidi"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${harList.c_time}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        </#list>
                    </w:tbl>
                    </#list>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:hint="default" w:eastAsiaTheme="minorEastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="13"/>
                                <w:szCs w:val="13"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="13"/>
                                <w:szCs w:val="13"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                            </w:rPr>
                            <w:t>行业能效情况</w:t>
                        </w:r>
                    </w:p>
                    <w:tbl>
                        <w:tblPr>
                            <w:tblStyle w:val="8"/>
                            <w:tblW w:w="0" w:type="auto"/>
                            <w:tblInd w:w="-976" w:type="dxa"/>
                            <w:tblBorders>
                                <w:top w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                <w:left w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                <w:bottom w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                <w:right w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                <w:insideH w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                <w:insideV w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                            </w:tblBorders>
                            <w:tblLayout w:type="autofit"/>
                            <w:tblCellMar>
                                <w:top w:w="0" w:type="dxa"/>
                                <w:left w:w="108" w:type="dxa"/>
                                <w:bottom w:w="0" w:type="dxa"/>
                                <w:right w:w="108" w:type="dxa"/>
                            </w:tblCellMar>
                        </w:tblPr>
                        <w:tblGrid>
                            <w:gridCol w:w="3088"/>
                            <w:gridCol w:w="3044"/>
                            <w:gridCol w:w="3366"/>
                        </w:tblGrid>
                        <w:tr>
                            <w:tblPrEx>
                                <w:tblBorders>
                                    <w:top w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:left w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:bottom w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:right w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideH w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideV w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                </w:tblBorders>
                                <w:tblCellMar>
                                    <w:top w:w="0" w:type="dxa"/>
                                    <w:left w:w="108" w:type="dxa"/>
                                    <w:bottom w:w="0" w:type="dxa"/>
                                    <w:right w:w="108" w:type="dxa"/>
                                </w:tblCellMar>
                            </w:tblPrEx>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3088" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>行业指标</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3044" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>吨电耗</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>(千瓦时/吨)</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3366" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>吨电费</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>(元/吨)</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        <w:tr>
                            <w:tblPrEx>
                                <w:tblBorders>
                                    <w:top w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:left w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:bottom w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:right w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideH w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideV w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                </w:tblBorders>
                                <w:tblCellMar>
                                    <w:top w:w="0" w:type="dxa"/>
                                    <w:left w:w="108" w:type="dxa"/>
                                    <w:bottom w:w="0" w:type="dxa"/>
                                    <w:right w:w="108" w:type="dxa"/>
                                </w:tblCellMar>
                            </w:tblPrEx>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3088" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>行业平均</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3044" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="8DB3E2" w:themeFill="text2"
                                           w:themeFillTint="66"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${industryEData.AVGPW}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3366" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${industryEData.AVGFEE}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        <w:tr>
                            <w:tblPrEx>
                                <w:tblBorders>
                                    <w:top w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:left w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:bottom w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:right w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideH w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideV w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                </w:tblBorders>
                                <w:tblCellMar>
                                    <w:top w:w="0" w:type="dxa"/>
                                    <w:left w:w="108" w:type="dxa"/>
                                    <w:bottom w:w="0" w:type="dxa"/>
                                    <w:right w:w="108" w:type="dxa"/>
                                </w:tblCellMar>
                            </w:tblPrEx>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3088" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>行业最优</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3044" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="8DB3E2" w:themeFill="text2"
                                           w:themeFillTint="66"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${industryEData.MINPW}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3366" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${industryEData.MINFEE}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        <w:tr>
                            <w:tblPrEx>
                                <w:tblBorders>
                                    <w:top w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:left w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:bottom w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:right w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideH w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideV w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                </w:tblBorders>
                                <w:tblCellMar>
                                    <w:top w:w="0" w:type="dxa"/>
                                    <w:left w:w="108" w:type="dxa"/>
                                    <w:bottom w:w="0" w:type="dxa"/>
                                    <w:right w:w="108" w:type="dxa"/>
                                </w:tblCellMar>
                            </w:tblPrEx>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3088" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>行业最差</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3044" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="8DB3E2" w:themeFill="text2"
                                           w:themeFillTint="66"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${industryEData.MAXPW}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3366" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${industryEData.MAXFEE}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        <w:tr>
                            <w:tblPrEx>
                                <w:tblBorders>
                                    <w:top w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:left w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:bottom w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:right w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideH w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideV w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                </w:tblBorders>
                                <w:tblCellMar>
                                    <w:top w:w="0" w:type="dxa"/>
                                    <w:left w:w="108" w:type="dxa"/>
                                    <w:bottom w:w="0" w:type="dxa"/>
                                    <w:right w:w="108" w:type="dxa"/>
                                </w:tblCellMar>
                            </w:tblPrEx>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3088" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>设备平均</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3044" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="8DB3E2" w:themeFill="text2"
                                           w:themeFillTint="66"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${industryEData.MAVGPW}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3366" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${industryEData.MAVGFEE}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        <w:tr>
                            <w:tblPrEx>
                                <w:tblBorders>
                                    <w:top w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:left w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:bottom w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:right w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideH w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideV w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                </w:tblBorders>
                                <w:tblCellMar>
                                    <w:top w:w="0" w:type="dxa"/>
                                    <w:left w:w="108" w:type="dxa"/>
                                    <w:bottom w:w="0" w:type="dxa"/>
                                    <w:right w:w="108" w:type="dxa"/>
                                </w:tblCellMar>
                            </w:tblPrEx>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3088" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>设备最优</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3044" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="8DB3E2" w:themeFill="text2"
                                           w:themeFillTint="66"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${industryEData.MMINPW}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3366" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${industryEData.MMINFEE}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        <w:tr>
                            <w:tblPrEx>
                                <w:tblBorders>
                                    <w:top w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:left w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:bottom w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:right w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideH w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideV w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                </w:tblBorders>
                                <w:tblCellMar>
                                    <w:top w:w="0" w:type="dxa"/>
                                    <w:left w:w="108" w:type="dxa"/>
                                    <w:bottom w:w="0" w:type="dxa"/>
                                    <w:right w:w="108" w:type="dxa"/>
                                </w:tblCellMar>
                            </w:tblPrEx>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3088" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>设备最差</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3044" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="8DB3E2" w:themeFill="text2"
                                           w:themeFillTint="66"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${industryEData.MMAXPW}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3366" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${industryEData.MMAXFEE}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                    </w:tbl>
                    <w:p>
                        <w:pPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="13"/>
                                <w:szCs w:val="13"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:hint="default" w:eastAsiaTheme="minorEastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="13"/>
                                <w:szCs w:val="13"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="13"/>
                                <w:szCs w:val="13"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                            </w:rPr>
                            <w:t>企业能效情况</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:pStyle w:val="6"/>
                            <w:keepNext w:val="0"/>
                            <w:keepLines w:val="0"/>
                            <w:widowControl/>
                            <w:suppressLineNumbers w:val="0"/>
                            <w:pBdr>
                                <w:top w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:left w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:bottom w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:right w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                            </w:pBdr>
                            <w:shd w:val="clear" w:fill="FFFFFF"/>
                            <w:spacing w:before="0" w:beforeAutospacing="0" w:after="0" w:afterAutospacing="0"/>
                            <w:ind w:left="0" w:right="0" w:firstLine="0"/>
                            <w:jc w:val="left"/>
                            <w:rPr>
                                <w:rFonts w:ascii="微软雅黑" w:hAnsi="微软雅黑" w:eastAsia="微软雅黑" w:cs="微软雅黑"/>
                                <w:i w:val="0"/>
                                <w:caps w:val="0"/>
                                <w:color w:val="4B4B4B"/>
                                <w:spacing w:val="0"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="微软雅黑" w:hAnsi="微软雅黑" w:eastAsia="微软雅黑"
                                          w:cs="微软雅黑"/>
                                <w:i w:val="0"/>
                                <w:caps w:val="0"/>
                                <w:color w:val="4B4B4B"/>
                                <w:spacing w:val="0"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                                <w:shd w:val="clear" w:fill="FFFFFF"/>
                            </w:rPr>
                            <w:t>${industryEData.pwText}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="微软雅黑" w:hAnsi="微软雅黑" w:eastAsia="微软雅黑"
                                          w:cs="微软雅黑"/>
                                <w:i w:val="0"/>
                                <w:caps w:val="0"/>
                                <w:color w:val="4B4B4B"/>
                                <w:spacing w:val="0"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                                <w:shd w:val="clear" w:fill="FFFFFF"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="微软雅黑" w:hAnsi="微软雅黑" w:eastAsia="微软雅黑"
                                          w:cs="微软雅黑"/>
                                <w:i w:val="0"/>
                                <w:caps w:val="0"/>
                                <w:color w:val="4B4B4B"/>
                                <w:spacing w:val="0"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                                <w:shd w:val="clear" w:fill="FFFFFF"/>
                            </w:rPr>
                            <w:t>${industryEData.feeText}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="微软雅黑" w:hAnsi="微软雅黑" w:eastAsia="微软雅黑"
                                          w:cs="微软雅黑"/>
                                <w:i w:val="0"/>
                                <w:caps w:val="0"/>
                                <w:color w:val="4B4B4B"/>
                                <w:spacing w:val="0"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                                <w:shd w:val="clear" w:fill="FFFFFF"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="微软雅黑" w:hAnsi="微软雅黑" w:eastAsia="微软雅黑"
                                          w:cs="微软雅黑"/>
                                <w:i w:val="0"/>
                                <w:caps w:val="0"/>
                                <w:color w:val="4B4B4B"/>
                                <w:spacing w:val="0"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                                <w:shd w:val="clear" w:fill="FFFFFF"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:hint="default" w:eastAsiaTheme="minorEastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="13"/>
                                <w:szCs w:val="13"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="13"/>
                                <w:szCs w:val="13"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                            </w:rPr>
                            <w:t>企业设备能效情况</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:drawing>
                                <wp:inline distT="0" distB="0" distL="114300" distR="114300">
                                    <wp:extent cx="5269230" cy="1092200"/>
                                    <wp:effectExtent l="0" t="0" r="7620" b="12700"/>
                                    <wp:docPr id="9" name="图片 3"/>
                                    <wp:cNvGraphicFramePr>
                                        <a:graphicFrameLocks
                                                xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main"
                                                noChangeAspect="1"/>
                                    </wp:cNvGraphicFramePr>
                                    <a:graphic xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main">
                                        <a:graphicData uri="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                            <pic:pic
                                                    xmlns:pic="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                                <pic:nvPicPr>
                                                    <pic:cNvPr id="9" name="图片 3"/>
                                                    <pic:cNvPicPr>
                                                        <a:picLocks noChangeAspect="1"/>
                                                    </pic:cNvPicPr>
                                                </pic:nvPicPr>
                                                <pic:blipFill>
                                                    <a:blip r:embed="${chartEERid}"/>
                                                    <a:stretch>
                                                        <a:fillRect/>
                                                    </a:stretch>
                                                </pic:blipFill>
                                                <pic:spPr>
                                                    <a:xfrm>
                                                        <a:off x="0" y="0"/>
                                                        <a:ext cx="5269230" cy="1092200"/>
                                                    </a:xfrm>
                                                    <a:prstGeom prst="rect">
                                                        <a:avLst/>
                                                    </a:prstGeom>
                                                    <a:noFill/>
                                                    <a:ln>
                                                        <a:noFill/>
                                                    </a:ln>
                                                </pic:spPr>
                                            </pic:pic>
                                        </a:graphicData>
                                    </a:graphic>
                                </wp:inline>
                            </w:drawing>
                        </w:r>
                    </w:p>
                    <w:tbl>
                        <w:tblPr>
                            <w:tblStyle w:val="8"/>
                            <w:tblW w:w="0" w:type="auto"/>
                            <w:tblInd w:w="-976" w:type="dxa"/>
                            <w:tblBorders>
                                <w:top w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                <w:left w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                <w:bottom w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                <w:right w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                <w:insideH w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                <w:insideV w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                            </w:tblBorders>
                            <w:tblLayout w:type="autofit"/>
                            <w:tblCellMar>
                                <w:top w:w="0" w:type="dxa"/>
                                <w:left w:w="108" w:type="dxa"/>
                                <w:bottom w:w="0" w:type="dxa"/>
                                <w:right w:w="108" w:type="dxa"/>
                            </w:tblCellMar>
                        </w:tblPr>
                        <w:tblGrid>
                            <w:gridCol w:w="3088"/>
                            <w:gridCol w:w="3044"/>
                            <w:gridCol w:w="3366"/>
                        </w:tblGrid>
                        <w:tr>
                            <w:tblPrEx>
                                <w:tblBorders>
                                    <w:top w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:left w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:bottom w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:right w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideH w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideV w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                </w:tblBorders>
                                <w:tblCellMar>
                                    <w:top w:w="0" w:type="dxa"/>
                                    <w:left w:w="108" w:type="dxa"/>
                                    <w:bottom w:w="0" w:type="dxa"/>
                                    <w:right w:w="108" w:type="dxa"/>
                                </w:tblCellMar>
                            </w:tblPrEx>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3088" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>设备</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3044" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>吨电耗</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>(千瓦时/吨)</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3366" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="7F7F7F" w:themeFill="text1"
                                           w:themeFillTint="7F"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>吨电费</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="F1F1F1" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>(元/吨)</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        <#list meterEDatas as eList>
                        <w:tr>
                            <w:tblPrEx>
                                <w:tblBorders>
                                    <w:top w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:left w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:bottom w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:right w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideH w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                    <w:insideV w:val="single" w:color="auto" w:sz="4" w:space="0"/>
                                </w:tblBorders>
                                <w:tblCellMar>
                                    <w:top w:w="0" w:type="dxa"/>
                                    <w:left w:w="108" w:type="dxa"/>
                                    <w:bottom w:w="0" w:type="dxa"/>
                                    <w:right w:w="108" w:type="dxa"/>
                                </w:tblCellMar>
                            </w:tblPrEx>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3088" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${eList.METER_NAME}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3044" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="8DB3E2" w:themeFill="text2"
                                           w:themeFillTint="66"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${eList.DATA}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3366" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="C2D69B" w:themeFill="accent3"
                                           w:themeFillTint="99"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:eastAsiaTheme="minorEastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="15"/>
                                            <w:szCs w:val="15"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>${eList.FEEDATA}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        </#list>
                    </w:tbl>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:hint="default" w:eastAsiaTheme="minorEastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="13"/>
                                <w:szCs w:val="13"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="13"/>
                                <w:szCs w:val="13"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                            </w:rPr>
                            <w:t>设备用能分析</w:t>
                        </w:r>
                    </w:p>

                    <#list canUseParam as canUse>
                    <w:p>
                        <w:pPr>
                            <w:keepNext w:val="0"/>
                            <w:keepLines w:val="0"/>
                            <w:widowControl/>
                            <w:suppressLineNumbers w:val="0"/>
                            <w:pBdr>
                                <w:top w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:left w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:bottom w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:right w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                            </w:pBdr>
                            <w:shd w:val="clear" w:fill="FFFFFF"/>
                            <w:spacing w:before="0" w:beforeAutospacing="0" w:after="0" w:afterAutospacing="0"
                                       w:line="450" w:lineRule="atLeast"/>
                            <w:ind w:left="0" w:right="0" w:firstLine="0"/>
                            <w:jc w:val="left"/>
                            <w:rPr>
                                <w:rFonts w:ascii="微软雅黑" w:hAnsi="微软雅黑" w:eastAsia="微软雅黑" w:cs="微软雅黑"/>
                                <w:i w:val="0"/>
                                <w:caps w:val="0"/>
                                <w:color w:val="15A7E8"/>
                                <w:spacing w:val="0"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="微软雅黑" w:hAnsi="微软雅黑" w:eastAsia="微软雅黑"
                                          w:cs="微软雅黑"/>
                                <w:i w:val="0"/>
                                <w:caps w:val="0"/>
                                <w:color w:val="15A7E8"/>
                                <w:spacing w:val="0"/>
                                <w:kern w:val="0"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                                <w:shd w:val="clear" w:fill="FFFFFF"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                            </w:rPr>
                            <w:t>负载率分析</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:pStyle w:val="6"/>
                            <w:keepNext w:val="0"/>
                            <w:keepLines w:val="0"/>
                            <w:widowControl/>
                            <w:suppressLineNumbers w:val="0"/>
                            <w:pBdr>
                                <w:top w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:left w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:bottom w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:right w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                            </w:pBdr>
                            <w:spacing w:before="0" w:beforeAutospacing="0" w:after="0" w:afterAutospacing="0"/>
                            <w:ind w:left="0" w:right="0"/>
                            <w:jc w:val="left"/>
                            <w:rPr>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="微软雅黑" w:hAnsi="微软雅黑" w:eastAsia="微软雅黑"
                                          w:cs="微软雅黑"/>
                                <w:i w:val="0"/>
                                <w:caps w:val="0"/>
                                <w:color w:val="4B4B4B"/>
                                <w:spacing w:val="0"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                                <w:shd w:val="clear" w:fill="FFFFFF"/>
                            </w:rPr>
                            <w:t>${canUse.loadFirstP}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:pStyle w:val="6"/>
                            <w:keepNext w:val="0"/>
                            <w:keepLines w:val="0"/>
                            <w:widowControl/>
                            <w:suppressLineNumbers w:val="0"/>
                            <w:pBdr>
                                <w:top w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:left w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:bottom w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:right w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                            </w:pBdr>
                            <w:spacing w:before="0" w:beforeAutospacing="0" w:after="0" w:afterAutospacing="0"/>
                            <w:ind w:left="0" w:right="0"/>
                            <w:jc w:val="left"/>
                            <w:rPr>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="微软雅黑" w:hAnsi="微软雅黑" w:eastAsia="微软雅黑"
                                          w:cs="微软雅黑"/>
                                <w:i w:val="0"/>
                                <w:caps w:val="0"/>
                                <w:color w:val="4B4B4B"/>
                                <w:spacing w:val="0"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                                <w:shd w:val="clear" w:fill="FFFFFF"/>
                            </w:rPr>
                            <w:t>${canUse.loadSecondP}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:pStyle w:val="6"/>
                            <w:keepNext w:val="0"/>
                            <w:keepLines w:val="0"/>
                            <w:widowControl/>
                            <w:suppressLineNumbers w:val="0"/>
                            <w:pBdr>
                                <w:top w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:left w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:bottom w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:right w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                            </w:pBdr>
                            <w:spacing w:before="0" w:beforeAutospacing="0" w:after="0" w:afterAutospacing="0"/>
                            <w:ind w:left="0" w:right="0"/>
                            <w:jc w:val="left"/>
                            <w:rPr>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="微软雅黑" w:hAnsi="微软雅黑" w:eastAsia="微软雅黑"
                                          w:cs="微软雅黑"/>
                                <w:i w:val="0"/>
                                <w:caps w:val="0"/>
                                <w:color w:val="4B4B4B"/>
                                <w:spacing w:val="0"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                                <w:shd w:val="clear" w:fill="FFFFFF"/>
                            </w:rPr>
                            <w:t>${canUse.loadThirdP}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:r>
                            <w:drawing>
                                <wp:inline distT="0" distB="0" distL="114300" distR="114300">
                                    <wp:extent cx="5270500" cy="1431290"/>
                                    <wp:effectExtent l="0" t="0" r="6350" b="16510"/>
                                    <wp:docPr id="10" name="图片 4"/>
                                    <wp:cNvGraphicFramePr>
                                        <a:graphicFrameLocks
                                                xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main"
                                                noChangeAspect="1"/>
                                    </wp:cNvGraphicFramePr>
                                    <a:graphic xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main">
                                        <a:graphicData uri="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                            <pic:pic
                                                    xmlns:pic="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                                <pic:nvPicPr>
                                                    <pic:cNvPr id="10" name="图片 4"/>
                                                    <pic:cNvPicPr>
                                                        <a:picLocks noChangeAspect="1"/>
                                                    </pic:cNvPicPr>
                                                </pic:nvPicPr>
                                                <pic:blipFill>
                                                    <a:blip r:embed="${canUse.maxLoadrId}"/>
                                                    <a:stretch>
                                                        <a:fillRect/>
                                                    </a:stretch>
                                                </pic:blipFill>
                                                <pic:spPr>
                                                    <a:xfrm>
                                                        <a:off x="0" y="0"/>
                                                        <a:ext cx="5270500" cy="1431290"/>
                                                    </a:xfrm>
                                                    <a:prstGeom prst="rect">
                                                        <a:avLst/>
                                                    </a:prstGeom>
                                                    <a:noFill/>
                                                    <a:ln>
                                                        <a:noFill/>
                                                    </a:ln>
                                                </pic:spPr>
                                            </pic:pic>
                                        </a:graphicData>
                                    </a:graphic>
                                </wp:inline>
                            </w:drawing>
                        </w:r>
                    </w:p>
                    <w:p/>
                    <w:p>
                        <w:r>
                            <w:drawing>
                                <wp:inline distT="0" distB="0" distL="114300" distR="114300">
                                    <wp:extent cx="5266055" cy="1394460"/>
                                    <wp:effectExtent l="0" t="0" r="10795" b="15240"/>
                                    <wp:docPr id="11" name="图片 5"/>
                                    <wp:cNvGraphicFramePr>
                                        <a:graphicFrameLocks
                                                xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main"
                                                noChangeAspect="1"/>
                                    </wp:cNvGraphicFramePr>
                                    <a:graphic xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main">
                                        <a:graphicData uri="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                            <pic:pic
                                                    xmlns:pic="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                                <pic:nvPicPr>
                                                    <pic:cNvPr id="11" name="图片 5"/>
                                                    <pic:cNvPicPr>
                                                        <a:picLocks noChangeAspect="1"/>
                                                    </pic:cNvPicPr>
                                                </pic:nvPicPr>
                                                <pic:blipFill>
                                                    <a:blip r:embed="${canUse.maxTimerId}"/>
                                                    <a:stretch>
                                                        <a:fillRect/>
                                                    </a:stretch>
                                                </pic:blipFill>
                                                <pic:spPr>
                                                    <a:xfrm>
                                                        <a:off x="0" y="0"/>
                                                        <a:ext cx="5266055" cy="1394460"/>
                                                    </a:xfrm>
                                                    <a:prstGeom prst="rect">
                                                        <a:avLst/>
                                                    </a:prstGeom>
                                                    <a:noFill/>
                                                    <a:ln>
                                                        <a:noFill/>
                                                    </a:ln>
                                                </pic:spPr>
                                            </pic:pic>
                                        </a:graphicData>
                                    </a:graphic>
                                </wp:inline>
                            </w:drawing>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:keepNext w:val="0"/>
                            <w:keepLines w:val="0"/>
                            <w:widowControl/>
                            <w:suppressLineNumbers w:val="0"/>
                            <w:pBdr>
                                <w:top w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:left w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:bottom w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:right w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                            </w:pBdr>
                            <w:shd w:val="clear" w:fill="FFFFFF"/>
                            <w:spacing w:before="0" w:beforeAutospacing="0" w:after="0" w:afterAutospacing="0"
                                       w:line="450" w:lineRule="atLeast"/>
                            <w:ind w:left="0" w:right="0" w:firstLine="0"/>
                            <w:jc w:val="left"/>
                            <w:rPr>
                                <w:rFonts w:ascii="微软雅黑" w:hAnsi="微软雅黑" w:eastAsia="微软雅黑" w:cs="微软雅黑"/>
                                <w:i w:val="0"/>
                                <w:caps w:val="0"/>
                                <w:color w:val="15A7E8"/>
                                <w:spacing w:val="0"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="微软雅黑" w:hAnsi="微软雅黑" w:eastAsia="微软雅黑"
                                          w:cs="微软雅黑"/>
                                <w:i w:val="0"/>
                                <w:caps w:val="0"/>
                                <w:color w:val="15A7E8"/>
                                <w:spacing w:val="0"/>
                                <w:kern w:val="0"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                                <w:shd w:val="clear" w:fill="FFFFFF"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                            </w:rPr>
                            <w:t>日不平衡度分析(电流)</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:pStyle w:val="6"/>
                            <w:keepNext w:val="0"/>
                            <w:keepLines w:val="0"/>
                            <w:widowControl/>
                            <w:suppressLineNumbers w:val="0"/>
                            <w:pBdr>
                                <w:top w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:left w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:bottom w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:right w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                            </w:pBdr>
                            <w:spacing w:before="0" w:beforeAutospacing="0" w:after="0" w:afterAutospacing="0"/>
                            <w:ind w:left="0" w:right="0"/>
                            <w:jc w:val="left"/>
                            <w:rPr>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="微软雅黑" w:hAnsi="微软雅黑" w:eastAsia="微软雅黑"
                                          w:cs="微软雅黑"/>
                                <w:i w:val="0"/>
                                <w:caps w:val="0"/>
                                <w:color w:val="4B4B4B"/>
                                <w:spacing w:val="0"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                                <w:shd w:val="clear" w:fill="FFFFFF"/>
                            </w:rPr>
                            <w:t>${canUse.msgIFirstP}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:pStyle w:val="6"/>
                            <w:keepNext w:val="0"/>
                            <w:keepLines w:val="0"/>
                            <w:widowControl/>
                            <w:suppressLineNumbers w:val="0"/>
                            <w:pBdr>
                                <w:top w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:left w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:bottom w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:right w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                            </w:pBdr>
                            <w:spacing w:before="0" w:beforeAutospacing="0" w:after="0" w:afterAutospacing="0"/>
                            <w:ind w:left="0" w:right="0"/>
                            <w:jc w:val="left"/>
                            <w:rPr>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="微软雅黑" w:hAnsi="微软雅黑" w:eastAsia="微软雅黑"
                                          w:cs="微软雅黑"/>
                                <w:i w:val="0"/>
                                <w:caps w:val="0"/>
                                <w:color w:val="4B4B4B"/>
                                <w:spacing w:val="0"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                                <w:shd w:val="clear" w:fill="FFFFFF"/>
                            </w:rPr>
                            <w:t>${canUse.msgISecondP}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:pStyle w:val="6"/>
                            <w:keepNext w:val="0"/>
                            <w:keepLines w:val="0"/>
                            <w:widowControl/>
                            <w:suppressLineNumbers w:val="0"/>
                            <w:pBdr>
                                <w:top w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:left w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:bottom w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:right w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                            </w:pBdr>
                            <w:spacing w:before="0" w:beforeAutospacing="0" w:after="0" w:afterAutospacing="0"/>
                            <w:ind w:left="0" w:right="0"/>
                            <w:jc w:val="left"/>
                            <w:rPr>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="微软雅黑" w:hAnsi="微软雅黑" w:eastAsia="微软雅黑"
                                          w:cs="微软雅黑"/>
                                <w:i w:val="0"/>
                                <w:caps w:val="0"/>
                                <w:color w:val="4B4B4B"/>
                                <w:spacing w:val="0"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                                <w:shd w:val="clear" w:fill="FFFFFF"/>
                            </w:rPr>
                            <w:t>${canUse.msgIThirdP}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:r>
                            <w:drawing>
                                <wp:inline distT="0" distB="0" distL="114300" distR="114300">
                                    <wp:extent cx="5268595" cy="2167255"/>
                                    <wp:effectExtent l="0" t="0" r="8255" b="4445"/>
                                    <wp:docPr id="12" name="图片 6"/>
                                    <wp:cNvGraphicFramePr>
                                        <a:graphicFrameLocks
                                                xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main"
                                                noChangeAspect="1"/>
                                    </wp:cNvGraphicFramePr>
                                    <a:graphic xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main">
                                        <a:graphicData uri="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                            <pic:pic
                                                    xmlns:pic="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                                <pic:nvPicPr>
                                                    <pic:cNvPr id="12" name="图片 6"/>
                                                    <pic:cNvPicPr>
                                                        <a:picLocks noChangeAspect="1"/>
                                                    </pic:cNvPicPr>
                                                </pic:nvPicPr>
                                                <pic:blipFill>
                                                    <a:blip r:embed="${canUse.maxIToprId}"/>
                                                    <a:stretch>
                                                        <a:fillRect/>
                                                    </a:stretch>
                                                </pic:blipFill>
                                                <pic:spPr>
                                                    <a:xfrm>
                                                        <a:off x="0" y="0"/>
                                                        <a:ext cx="5268595" cy="2167255"/>
                                                    </a:xfrm>
                                                    <a:prstGeom prst="rect">
                                                        <a:avLst/>
                                                    </a:prstGeom>
                                                    <a:noFill/>
                                                    <a:ln>
                                                        <a:noFill/>
                                                    </a:ln>
                                                </pic:spPr>
                                            </pic:pic>
                                        </a:graphicData>
                                    </a:graphic>
                                </wp:inline>
                            </w:drawing>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:r>
                            <w:drawing>
                                <wp:inline distT="0" distB="0" distL="114300" distR="114300">
                                    <wp:extent cx="5269230" cy="2199005"/>
                                    <wp:effectExtent l="0" t="0" r="7620" b="10795"/>
                                    <wp:docPr id="13" name="图片 7"/>
                                    <wp:cNvGraphicFramePr>
                                        <a:graphicFrameLocks
                                                xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main"
                                                noChangeAspect="1"/>
                                    </wp:cNvGraphicFramePr>
                                    <a:graphic xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main">
                                        <a:graphicData uri="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                            <pic:pic
                                                    xmlns:pic="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                                <pic:nvPicPr>
                                                    <pic:cNvPr id="13" name="图片 7"/>
                                                    <pic:cNvPicPr>
                                                        <a:picLocks noChangeAspect="1"/>
                                                    </pic:cNvPicPr>
                                                </pic:nvPicPr>
                                                <pic:blipFill>
                                                    <a:blip r:embed="${canUse.maxItimerId}"/>
                                                    <a:stretch>
                                                        <a:fillRect/>
                                                    </a:stretch>
                                                </pic:blipFill>
                                                <pic:spPr>
                                                    <a:xfrm>
                                                        <a:off x="0" y="0"/>
                                                        <a:ext cx="5269230" cy="2199005"/>
                                                    </a:xfrm>
                                                    <a:prstGeom prst="rect">
                                                        <a:avLst/>
                                                    </a:prstGeom>
                                                    <a:noFill/>
                                                    <a:ln>
                                                        <a:noFill/>
                                                    </a:ln>
                                                </pic:spPr>
                                            </pic:pic>
                                        </a:graphicData>
                                    </a:graphic>
                                </wp:inline>
                            </w:drawing>
                        </w:r>
                        <w:r>
                            <w:drawing>
                                <wp:inline distT="0" distB="0" distL="114300" distR="114300">
                                    <wp:extent cx="5210175" cy="2276475"/>
                                    <wp:effectExtent l="0" t="0" r="9525" b="9525"/>
                                    <wp:docPr id="14" name="图片 8"/>
                                    <wp:cNvGraphicFramePr>
                                        <a:graphicFrameLocks
                                                xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main"
                                                noChangeAspect="1"/>
                                    </wp:cNvGraphicFramePr>
                                    <a:graphic xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main">
                                        <a:graphicData uri="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                            <pic:pic
                                                    xmlns:pic="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                                <pic:nvPicPr>
                                                    <pic:cNvPr id="14" name="图片 8"/>
                                                    <pic:cNvPicPr>
                                                        <a:picLocks noChangeAspect="1"/>
                                                    </pic:cNvPicPr>
                                                </pic:nvPicPr>
                                                <pic:blipFill>
                                                    <a:blip r:embed="${canUse.sumItimerId}"/>
                                                    <a:stretch>
                                                        <a:fillRect/>
                                                    </a:stretch>
                                                </pic:blipFill>
                                                <pic:spPr>
                                                    <a:xfrm>
                                                        <a:off x="0" y="0"/>
                                                        <a:ext cx="5210175" cy="2276475"/>
                                                    </a:xfrm>
                                                    <a:prstGeom prst="rect">
                                                        <a:avLst/>
                                                    </a:prstGeom>
                                                    <a:noFill/>
                                                    <a:ln>
                                                        <a:noFill/>
                                                    </a:ln>
                                                </pic:spPr>
                                            </pic:pic>
                                        </a:graphicData>
                                    </a:graphic>
                                </wp:inline>
                            </w:drawing>
                        </w:r>
                    </w:p>
                    <w:p/>
                    <w:p>
                        <w:pPr>
                            <w:keepNext w:val="0"/>
                            <w:keepLines w:val="0"/>
                            <w:widowControl/>
                            <w:suppressLineNumbers w:val="0"/>
                            <w:pBdr>
                                <w:top w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:left w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:bottom w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:right w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                            </w:pBdr>
                            <w:shd w:val="clear" w:fill="FFFFFF"/>
                            <w:spacing w:before="0" w:beforeAutospacing="0" w:after="0" w:afterAutospacing="0"
                                       w:line="450" w:lineRule="atLeast"/>
                            <w:ind w:left="0" w:right="0" w:firstLine="0"/>
                            <w:jc w:val="left"/>
                            <w:rPr>
                                <w:rFonts w:ascii="微软雅黑" w:hAnsi="微软雅黑" w:eastAsia="微软雅黑" w:cs="微软雅黑"/>
                                <w:i w:val="0"/>
                                <w:caps w:val="0"/>
                                <w:color w:val="15A7E8"/>
                                <w:spacing w:val="0"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="微软雅黑" w:hAnsi="微软雅黑" w:eastAsia="微软雅黑"
                                          w:cs="微软雅黑"/>
                                <w:i w:val="0"/>
                                <w:caps w:val="0"/>
                                <w:color w:val="15A7E8"/>
                                <w:spacing w:val="0"/>
                                <w:kern w:val="0"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                                <w:shd w:val="clear" w:fill="FFFFFF"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                            </w:rPr>
                            <w:t>日不平衡度分析(电压)</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:pStyle w:val="6"/>
                            <w:keepNext w:val="0"/>
                            <w:keepLines w:val="0"/>
                            <w:widowControl/>
                            <w:suppressLineNumbers w:val="0"/>
                            <w:pBdr>
                                <w:top w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:left w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:bottom w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:right w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                            </w:pBdr>
                            <w:spacing w:before="0" w:beforeAutospacing="0" w:after="0" w:afterAutospacing="0"/>
                            <w:ind w:left="0" w:right="0"/>
                            <w:jc w:val="left"/>
                            <w:rPr>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="微软雅黑" w:hAnsi="微软雅黑" w:eastAsia="微软雅黑"
                                          w:cs="微软雅黑"/>
                                <w:i w:val="0"/>
                                <w:caps w:val="0"/>
                                <w:color w:val="4B4B4B"/>
                                <w:spacing w:val="0"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                                <w:shd w:val="clear" w:fill="FFFFFF"/>
                            </w:rPr>
                            <w:t>${canUse.msgVFirstP}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:pStyle w:val="6"/>
                            <w:keepNext w:val="0"/>
                            <w:keepLines w:val="0"/>
                            <w:widowControl/>
                            <w:suppressLineNumbers w:val="0"/>
                            <w:pBdr>
                                <w:top w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:left w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:bottom w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:right w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                            </w:pBdr>
                            <w:spacing w:before="0" w:beforeAutospacing="0" w:after="0" w:afterAutospacing="0"/>
                            <w:ind w:left="0" w:right="0"/>
                            <w:jc w:val="left"/>
                            <w:rPr>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="微软雅黑" w:hAnsi="微软雅黑" w:eastAsia="微软雅黑"
                                          w:cs="微软雅黑"/>
                                <w:i w:val="0"/>
                                <w:caps w:val="0"/>
                                <w:color w:val="4B4B4B"/>
                                <w:spacing w:val="0"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                                <w:shd w:val="clear" w:fill="FFFFFF"/>
                            </w:rPr>
                            <w:t>${canUse.msgVSecondP}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:pStyle w:val="6"/>
                            <w:keepNext w:val="0"/>
                            <w:keepLines w:val="0"/>
                            <w:widowControl/>
                            <w:suppressLineNumbers w:val="0"/>
                            <w:pBdr>
                                <w:top w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:left w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:bottom w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:right w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                            </w:pBdr>
                            <w:spacing w:before="0" w:beforeAutospacing="0" w:after="0" w:afterAutospacing="0"/>
                            <w:ind w:left="0" w:right="0"/>
                            <w:jc w:val="left"/>
                            <w:rPr>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="微软雅黑" w:hAnsi="微软雅黑" w:eastAsia="微软雅黑"
                                          w:cs="微软雅黑"/>
                                <w:i w:val="0"/>
                                <w:caps w:val="0"/>
                                <w:color w:val="4B4B4B"/>
                                <w:spacing w:val="0"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                                <w:shd w:val="clear" w:fill="FFFFFF"/>
                            </w:rPr>
                            <w:t>${canUse.msgVThirdP}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:drawing>
                                <wp:inline distT="0" distB="0" distL="114300" distR="114300">
                                    <wp:extent cx="5272405" cy="1986915"/>
                                    <wp:effectExtent l="0" t="0" r="4445" b="13335"/>
                                    <wp:docPr id="15" name="图片 9"/>
                                    <wp:cNvGraphicFramePr>
                                        <a:graphicFrameLocks
                                                xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main"
                                                noChangeAspect="1"/>
                                    </wp:cNvGraphicFramePr>
                                    <a:graphic xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main">
                                        <a:graphicData uri="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                            <pic:pic
                                                    xmlns:pic="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                                <pic:nvPicPr>
                                                    <pic:cNvPr id="15" name="图片 9"/>
                                                    <pic:cNvPicPr>
                                                        <a:picLocks noChangeAspect="1"/>
                                                    </pic:cNvPicPr>
                                                </pic:nvPicPr>
                                                <pic:blipFill>
                                                    <a:blip r:embed="${canUse.maxVtoprId}"/>
                                                    <a:stretch>
                                                        <a:fillRect/>
                                                    </a:stretch>
                                                </pic:blipFill>
                                                <pic:spPr>
                                                    <a:xfrm>
                                                        <a:off x="0" y="0"/>
                                                        <a:ext cx="5272405" cy="1986915"/>
                                                    </a:xfrm>
                                                    <a:prstGeom prst="rect">
                                                        <a:avLst/>
                                                    </a:prstGeom>
                                                    <a:noFill/>
                                                    <a:ln>
                                                        <a:noFill/>
                                                    </a:ln>
                                                </pic:spPr>
                                            </pic:pic>
                                        </a:graphicData>
                                    </a:graphic>
                                </wp:inline>
                            </w:drawing>
                        </w:r>
                        <w:r>
                            <w:drawing>
                                <wp:inline distT="0" distB="0" distL="114300" distR="114300">
                                    <wp:extent cx="5271135" cy="2272665"/>
                                    <wp:effectExtent l="0" t="0" r="5715" b="13335"/>
                                    <wp:docPr id="16" name="图片 10"/>
                                    <wp:cNvGraphicFramePr>
                                        <a:graphicFrameLocks
                                                xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main"
                                                noChangeAspect="1"/>
                                    </wp:cNvGraphicFramePr>
                                    <a:graphic xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main">
                                        <a:graphicData uri="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                            <pic:pic
                                                    xmlns:pic="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                                <pic:nvPicPr>
                                                    <pic:cNvPr id="16" name="图片 10"/>
                                                    <pic:cNvPicPr>
                                                        <a:picLocks noChangeAspect="1"/>
                                                    </pic:cNvPicPr>
                                                </pic:nvPicPr>
                                                <pic:blipFill>
                                                    <a:blip r:embed="${canUse.maxVtimerId}"/>
                                                    <a:stretch>
                                                        <a:fillRect/>
                                                    </a:stretch>
                                                </pic:blipFill>
                                                <pic:spPr>
                                                    <a:xfrm>
                                                        <a:off x="0" y="0"/>
                                                        <a:ext cx="5271135" cy="2272665"/>
                                                    </a:xfrm>
                                                    <a:prstGeom prst="rect">
                                                        <a:avLst/>
                                                    </a:prstGeom>
                                                    <a:noFill/>
                                                    <a:ln>
                                                        <a:noFill/>
                                                    </a:ln>
                                                </pic:spPr>
                                            </pic:pic>
                                        </a:graphicData>
                                    </a:graphic>
                                </wp:inline>
                            </w:drawing>
                        </w:r>
                        <w:r>
                            <w:drawing>
                                <wp:inline distT="0" distB="0" distL="114300" distR="114300">
                                    <wp:extent cx="5269230" cy="2226945"/>
                                    <wp:effectExtent l="0" t="0" r="7620" b="1905"/>
                                    <wp:docPr id="17" name="图片 11"/>
                                    <wp:cNvGraphicFramePr>
                                        <a:graphicFrameLocks
                                                xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main"
                                                noChangeAspect="1"/>
                                    </wp:cNvGraphicFramePr>
                                    <a:graphic xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main">
                                        <a:graphicData uri="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                            <pic:pic
                                                    xmlns:pic="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                                <pic:nvPicPr>
                                                    <pic:cNvPr id="17" name="图片 11"/>
                                                    <pic:cNvPicPr>
                                                        <a:picLocks noChangeAspect="1"/>
                                                    </pic:cNvPicPr>
                                                </pic:nvPicPr>
                                                <pic:blipFill>
                                                    <a:blip r:embed="${canUse.sumVtimerId}"/>
                                                    <a:stretch>
                                                        <a:fillRect/>
                                                    </a:stretch>
                                                </pic:blipFill>
                                                <pic:spPr>
                                                    <a:xfrm>
                                                        <a:off x="0" y="0"/>
                                                        <a:ext cx="5269230" cy="2226945"/>
                                                    </a:xfrm>
                                                    <a:prstGeom prst="rect">
                                                        <a:avLst/>
                                                    </a:prstGeom>
                                                    <a:noFill/>
                                                    <a:ln>
                                                        <a:noFill/>
                                                    </a:ln>
                                                </pic:spPr>
                                            </pic:pic>
                                        </a:graphicData>
                                    </a:graphic>
                                </wp:inline>
                            </w:drawing>
                        </w:r>
                    </w:p>
                    </#list>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                            <w:t>专家建议：</w:t>
                        </w:r>
                        <w:bookmarkStart w:id="0" w:name="_GoBack"/>
                        <w:bookmarkEnd w:id="0"/>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:color w:val="548DD4" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:jc w:val="right"/>
                            <w:rPr>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="15"/>
                                <w:szCs w:val="15"/>
                            </w:rPr>
                            <w:t>专家姓名： 专家电话：</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:rPr>
                                <w:sz w:val="13"/>
                                <w:szCs w:val="13"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:sectPr>
                        <w:pgSz w:w="11906" w:h="16838"/>
                        <w:pgMar w:top="1440" w:right="1800" w:bottom="1440" w:left="1800" w:header="851" w:footer="992"
                                 w:gutter="0"/>
                        <w:cols w:space="425" w:num="1"/>
                        <w:docGrid w:type="lines" w:linePitch="312" w:charSpace="0"/>
                    </w:sectPr>
                </w:body>
            </w:document>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/customXml/_rels/item1.xml.rels"
              pkg:contentType="application/vnd.openxmlformats-package.relationships+xml">
        <pkg:xmlData>
            <Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
                <Relationship Id="rId1"
                              Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/customXmlProps"
                              Target="itemProps1.xml"/>
            </Relationships>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/customXml/item1.xml" pkg:contentType="application/xml">
        <pkg:xmlData>
            <s:customData xmlns="http://www.wps.cn/officeDocument/2013/wpsCustomData"
                          xmlns:s="http://www.wps.cn/officeDocument/2013/wpsCustomData">
                <customSectProps>
                    <customSectPr/>
                </customSectProps>
            </s:customData>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/customXml/itemProps1.xml"
              pkg:contentType="application/vnd.openxmlformats-officedocument.customXmlProperties+xml">
        <pkg:xmlData>
            <ds:datastoreItem ds:itemID="{B1977F7D-205B-4081-913C-38D41E755F92}"
                              xmlns:ds="http://schemas.openxmlformats.org/officeDocument/2006/customXml">
                <ds:schemaRefs>
                    <ds:schemaRef ds:uri="http://www.wps.cn/officeDocument/2013/wpsCustomData"/>
                </ds:schemaRefs>
            </ds:datastoreItem>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/docProps/app.xml"
              pkg:contentType="application/vnd.openxmlformats-officedocument.extended-properties+xml">
        <pkg:xmlData>
            <Properties xmlns="http://schemas.openxmlformats.org/officeDocument/2006/extended-properties"
                        xmlns:vt="http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes">
                <Template>Normal.dotm</Template>
                <Company>Sky123.Org</Company>
                <Pages>2</Pages>
                <Words>78</Words>
                <Characters>446</Characters>
                <Lines>3</Lines>
                <Paragraphs>1</Paragraphs>
                <TotalTime>166</TotalTime>
                <ScaleCrop>false</ScaleCrop>
                <LinksUpToDate>false</LinksUpToDate>
                <CharactersWithSpaces>523</CharactersWithSpaces>
                <Application>WPS Office_11.1.0.10228_F1E327BC-269C-435d-A152-05C5408002CA</Application>
                <DocSecurity>0</DocSecurity>
            </Properties>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/docProps/core.xml"
              pkg:contentType="application/vnd.openxmlformats-package.core-properties+xml">
        <pkg:xmlData>
            <cp:coreProperties xmlns:cp="http://schemas.openxmlformats.org/package/2006/metadata/core-properties"
                               xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:dcterms="http://purl.org/dc/terms/"
                               xmlns:dcmitype="http://purl.org/dc/dcmitype/"
                               xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                <dcterms:created xsi:type="dcterms:W3CDTF">2016-09-27T11:01:00Z</dcterms:created>
                <dc:creator>Sky123.Org</dc:creator>
                <cp:lastModifiedBy>Administrator</cp:lastModifiedBy>
                <dcterms:modified xsi:type="dcterms:W3CDTF">2020-12-18T06:07:43Z</dcterms:modified>
                <cp:revision>2</cp:revision>
            </cp:coreProperties>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/docProps/custom.xml"
              pkg:contentType="application/vnd.openxmlformats-officedocument.custom-properties+xml">
        <pkg:xmlData>
            <Properties xmlns="http://schemas.openxmlformats.org/officeDocument/2006/custom-properties"
                        xmlns:vt="http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes">
                <property fmtid="{D5CDD505-2E9C-101B-9397-08002B2CF9AE}" pid="2" name="KSOProductBuildVer">
                    <vt:lpwstr>2052-11.1.0.10228</vt:lpwstr>
                </property>
            </Properties>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/word/fontTable.xml"
              pkg:contentType="application/vnd.openxmlformats-officedocument.wordprocessingml.fontTable+xml">
        <pkg:xmlData>
            <w:fonts xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006"
                     xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships"
                     xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main"
                     xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml" mc:Ignorable="w14">
                <w:font w:name="Times New Roman">
                    <w:panose1 w:val="02020603050405020304"/>
                    <w:charset w:val="00"/>
                    <w:family w:val="roman"/>
                    <w:pitch w:val="variable"/>
                    <w:sig w:usb0="20007A87" w:usb1="80000000" w:usb2="00000008" w:usb3="00000000" w:csb0="000001FF"
                           w:csb1="00000000"/>
                </w:font>
                <w:font w:name="宋体">
                    <w:panose1 w:val="02010600030101010101"/>
                    <w:charset w:val="86"/>
                    <w:family w:val="auto"/>
                    <w:pitch w:val="default"/>
                    <w:sig w:usb0="00000003" w:usb1="288F0000" w:usb2="00000006" w:usb3="00000000" w:csb0="00040001"
                           w:csb1="00000000"/>
                </w:font>
                <w:font w:name="Wingdings">
                    <w:panose1 w:val="05000000000000000000"/>
                    <w:charset w:val="02"/>
                    <w:family w:val="auto"/>
                    <w:pitch w:val="default"/>
                    <w:sig w:usb0="00000000" w:usb1="00000000" w:usb2="00000000" w:usb3="00000000" w:csb0="80000000"
                           w:csb1="00000000"/>
                </w:font>
                <w:font w:name="Arial">
                    <w:panose1 w:val="020B0604020202020204"/>
                    <w:charset w:val="01"/>
                    <w:family w:val="swiss"/>
                    <w:pitch w:val="default"/>
                    <w:sig w:usb0="E0002AFF" w:usb1="C0007843" w:usb2="00000009" w:usb3="00000000" w:csb0="400001FF"
                           w:csb1="FFFF0000"/>
                </w:font>
                <w:font w:name="黑体">
                    <w:panose1 w:val="02010609060101010101"/>
                    <w:charset w:val="86"/>
                    <w:family w:val="auto"/>
                    <w:pitch w:val="default"/>
                    <w:sig w:usb0="800002BF" w:usb1="38CF7CFA" w:usb2="00000016" w:usb3="00000000" w:csb0="00040001"
                           w:csb1="00000000"/>
                </w:font>
                <w:font w:name="Courier New">
                    <w:panose1 w:val="02070309020205020404"/>
                    <w:charset w:val="01"/>
                    <w:family w:val="modern"/>
                    <w:pitch w:val="default"/>
                    <w:sig w:usb0="E0002AFF" w:usb1="C0007843" w:usb2="00000009" w:usb3="00000000" w:csb0="400001FF"
                           w:csb1="FFFF0000"/>
                </w:font>
                <w:font w:name="Symbol">
                    <w:panose1 w:val="05050102010706020507"/>
                    <w:charset w:val="02"/>
                    <w:family w:val="roman"/>
                    <w:pitch w:val="default"/>
                    <w:sig w:usb0="00000000" w:usb1="00000000" w:usb2="00000000" w:usb3="00000000" w:csb0="80000000"
                           w:csb1="00000000"/>
                </w:font>
                <w:font w:name="Calibri">
                    <w:panose1 w:val="020F0502020204030204"/>
                    <w:charset w:val="00"/>
                    <w:family w:val="swiss"/>
                    <w:pitch w:val="default"/>
                    <w:sig w:usb0="E00002FF" w:usb1="4000ACFF" w:usb2="00000001" w:usb3="00000000" w:csb0="2000019F"
                           w:csb1="00000000"/>
                </w:font>
                <w:font w:name="微软雅黑">
                    <w:panose1 w:val="020B0503020204020204"/>
                    <w:charset w:val="86"/>
                    <w:family w:val="auto"/>
                    <w:pitch w:val="default"/>
                    <w:sig w:usb0="80000287" w:usb1="280F3C52" w:usb2="00000016" w:usb3="00000000" w:csb0="0004001F"
                           w:csb1="00000000"/>
                </w:font>
            </w:fonts>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/word/media/image1.png" pkg:contentType="image/png">
        <pkg:binaryData>${chart1}</pkg:binaryData>
    </pkg:part>
    <pkg:part pkg:name="/word/media/image2.png" pkg:contentType="image/png">
        <pkg:binaryData>${chart2}</pkg:binaryData>
    </pkg:part>
    <pkg:part pkg:name="/word/media/image3.png" pkg:contentType="image/png">
        <pkg:binaryData>${chart3}</pkg:binaryData>
    </pkg:part>
    <pkg:part pkg:name="/word/media/image4.png" pkg:contentType="image/png">
        <pkg:binaryData>${chart4}</pkg:binaryData>
    </pkg:part>
    <pkg:part pkg:name="/word/media/image5.png" pkg:contentType="image/png">
        <pkg:binaryData>${chartP}</pkg:binaryData>
    </pkg:part>
    <#list table3 as harPlan>
        <pkg:part pkg:name="/word/media/${harPlan.image}.png" pkg:contentType="image/png">
            <pkg:binaryData>${harPlan.harChart}</pkg:binaryData>
        </pkg:part>
    </#list>
    <#list table4 as harVPlan>
        <pkg:part pkg:name="/word/media/${harVPlan.image}.png" pkg:contentType="image/png">
            <pkg:binaryData>${harVPlan.harChart}</pkg:binaryData>
        </pkg:part>
    </#list>
    <pkg:part pkg:name="/word/media/${chartEEImage}.png" pkg:contentType="image/png">
        <pkg:binaryData>${chartEE}</pkg:binaryData>
    </pkg:part>
    <#list canUseParam as canUse>
    <pkg:part pkg:name="/word/media/${canUse.maxLoadimage}.png" pkg:contentType="image/png">
        <pkg:binaryData>${canUse.maxLoadChart}</pkg:binaryData>
    </pkg:part>
    <pkg:part pkg:name="/word/media/${canUse.maxTimeimage}.png" pkg:contentType="image/png">
        <pkg:binaryData>${canUse.maxTimeChart}</pkg:binaryData>
    </pkg:part>
    <pkg:part pkg:name="/word/media/${canUse.maxITopimage}.png" pkg:contentType="image/png">
        <pkg:binaryData>${canUse.maxItopChart}</pkg:binaryData>
    </pkg:part>
    <pkg:part pkg:name="/word/media/${canUse.maxItimeimage}.png" pkg:contentType="image/png">
        <pkg:binaryData>${canUse.maxItimeChart}</pkg:binaryData>
    </pkg:part>
    <pkg:part pkg:name="/word/media/${canUse.sumItimeimage}.png" pkg:contentType="image/png">
        <pkg:binaryData>${canUse.sumItimeChart}</pkg:binaryData>
    </pkg:part>
    <pkg:part pkg:name="/word/media/${canUse.maxVtopimage}.png" pkg:contentType="image/png">
        <pkg:binaryData>${canUse.maxVtopChart}</pkg:binaryData>
    </pkg:part>
    <pkg:part pkg:name="/word/media/${canUse.maxVtimeimage}.png" pkg:contentType="image/png">
        <pkg:binaryData>${canUse.maxVtimeChart}</pkg:binaryData>
    </pkg:part>
    <pkg:part pkg:name="/word/media/${canUse.sumVtimeimage}.png" pkg:contentType="image/png">
        <pkg:binaryData>${canUse.sumVtimeChart}</pkg:binaryData>
    </pkg:part>
    </#list>
    <pkg:part pkg:name="/word/settings.xml"
              pkg:contentType="application/vnd.openxmlformats-officedocument.wordprocessingml.settings+xml">
        <pkg:xmlData>
            <w:settings xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006"
                        xmlns:o="urn:schemas-microsoft-com:office:office"
                        xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships"
                        xmlns:m="http://schemas.openxmlformats.org/officeDocument/2006/math"
                        xmlns:v="urn:schemas-microsoft-com:vml" xmlns:w10="urn:schemas-microsoft-com:office:word"
                        xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main"
                        xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml"
                        xmlns:sl="http://schemas.openxmlformats.org/schemaLibrary/2006/main" mc:Ignorable="w14">
                <w:zoom w:percent="170"/>
                <w:doNotDisplayPageBoundaries w:val="1"/>
                <w:bordersDoNotSurroundHeader w:val="0"/>
                <w:bordersDoNotSurroundFooter w:val="0"/>
                <w:documentProtection w:enforcement="0"/>
                <w:defaultTabStop w:val="420"/>
                <w:drawingGridVerticalSpacing w:val="156"/>
                <w:displayHorizontalDrawingGridEvery w:val="1"/>
                <w:displayVerticalDrawingGridEvery w:val="1"/>
                <w:noPunctuationKerning w:val="1"/>
                <w:characterSpacingControl w:val="compressPunctuation"/>
                <w:compat>
                    <w:spaceForUL/>
                    <w:balanceSingleByteDoubleByteWidth/>
                    <w:doNotLeaveBackslashAlone/>
                    <w:ulTrailSpace/>
                    <w:doNotExpandShiftReturn/>
                    <w:adjustLineHeightInTable/>
                    <w:doNotWrapTextWithPunct/>
                    <w:doNotUseEastAsianBreakRules/>
                    <w:useFELayout/>
                    <w:doNotUseIndentAsNumberingTabStop/>
                    <w:useAltKinsokuLineBreakRules/>
                    <w:compatSetting w:name="compatibilityMode" w:uri="http://schemas.microsoft.com/office/word"
                                     w:val="12"/>
                </w:compat>
                <w:rsids>
                    <w:rsidRoot w:val="00F235D4"/>
                    <w:rsid w:val="00032F65"/>
                    <w:rsid w:val="0007660C"/>
                    <w:rsid w:val="000E07AF"/>
                    <w:rsid w:val="000F423D"/>
                    <w:rsid w:val="001C3016"/>
                    <w:rsid w:val="00221D64"/>
                    <w:rsid w:val="00262955"/>
                    <w:rsid w:val="002F2864"/>
                    <w:rsid w:val="00344AAD"/>
                    <w:rsid w:val="00380F11"/>
                    <w:rsid w:val="00381900"/>
                    <w:rsid w:val="00473003"/>
                    <w:rsid w:val="00546566"/>
                    <w:rsid w:val="005643B4"/>
                    <w:rsid w:val="005B1A01"/>
                    <w:rsid w:val="006115C3"/>
                    <w:rsid w:val="006659E7"/>
                    <w:rsid w:val="00693C0A"/>
                    <w:rsid w:val="006A3F86"/>
                    <w:rsid w:val="006A741A"/>
                    <w:rsid w:val="006B55A1"/>
                    <w:rsid w:val="007A75CA"/>
                    <w:rsid w:val="00855584"/>
                    <w:rsid w:val="008B09C9"/>
                    <w:rsid w:val="0094530D"/>
                    <w:rsid w:val="0096154E"/>
                    <w:rsid w:val="009C6E38"/>
                    <w:rsid w:val="009E306A"/>
                    <w:rsid w:val="00AE10FF"/>
                    <w:rsid w:val="00B96C70"/>
                    <w:rsid w:val="00C4594A"/>
                    <w:rsid w:val="00CD2E47"/>
                    <w:rsid w:val="00D70FA0"/>
                    <w:rsid w:val="00D76E13"/>
                    <w:rsid w:val="00D87E63"/>
                    <w:rsid w:val="00DA679C"/>
                    <w:rsid w:val="00DD7EE1"/>
                    <w:rsid w:val="00E4210E"/>
                    <w:rsid w:val="00E44D73"/>
                    <w:rsid w:val="00E475DA"/>
                    <w:rsid w:val="00EC50AC"/>
                    <w:rsid w:val="00F11D5B"/>
                    <w:rsid w:val="00F235D4"/>
                    <w:rsid w:val="00F741FF"/>
                    <w:rsid w:val="442B29FF"/>
                    <w:rsid w:val="5C126EC9"/>
                    <w:rsid w:val="7CEB2845"/>
                </w:rsids>
                <m:mathPr>
                    <m:mathFont m:val="Cambria Math"/>
                    <m:brkBin m:val="before"/>
                    <m:brkBinSub m:val="--"/>
                    <m:smallFrac m:val="0"/>
                    <m:dispDef/>
                    <m:lMargin m:val="0"/>
                    <m:rMargin m:val="0"/>
                    <m:defJc m:val="centerGroup"/>
                    <m:wrapIndent m:val="1440"/>
                    <m:intLim m:val="subSup"/>
                    <m:naryLim m:val="undOvr"/>
                </m:mathPr>
                <w:themeFontLang w:val="en-US" w:eastAsia="zh-CN"/>
                <w:clrSchemeMapping w:bg1="light1" w:t1="dark1" w:bg2="light2" w:t2="dark2" w:accent1="accent1"
                                    w:accent2="accent2" w:accent3="accent3" w:accent4="accent4" w:accent5="accent5"
                                    w:accent6="accent6" w:hyperlink="hyperlink"
                                    w:followedHyperlink="followedHyperlink"/>
                <w:doNotIncludeSubdocsInStats/>
            </w:settings>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/word/styles.xml"
              pkg:contentType="application/vnd.openxmlformats-officedocument.wordprocessingml.styles+xml">
        <pkg:xmlData>
            <w:styles xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006"
                      xmlns:o="urn:schemas-microsoft-com:office:office"
                      xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships"
                      xmlns:m="http://schemas.openxmlformats.org/officeDocument/2006/math"
                      xmlns:v="urn:schemas-microsoft-com:vml"
                      xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main"
                      xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml"
                      xmlns:w10="urn:schemas-microsoft-com:office:word"
                      xmlns:sl="http://schemas.openxmlformats.org/schemaLibrary/2006/main" mc:Ignorable="w14">
                <w:docDefaults>
                    <w:rPrDefault>
                        <w:rPr>
                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:eastAsia="宋体"
                                      w:cs="Times New Roman"/>
                        </w:rPr>
                    </w:rPrDefault>
                </w:docDefaults>
                <w:latentStyles w:count="260" w:defQFormat="0" w:defUnhideWhenUsed="1" w:defSemiHidden="1"
                                w:defUIPriority="99" w:defLockedState="0">
                    <w:lsdException w:qFormat="1" w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0"
                                    w:name="Normal"/>
                    <w:lsdException w:qFormat="1" w:unhideWhenUsed="0" w:uiPriority="9" w:semiHidden="0"
                                    w:name="heading 1"/>
                    <w:lsdException w:qFormat="1" w:uiPriority="9" w:name="heading 2"/>
                    <w:lsdException w:qFormat="1" w:uiPriority="9" w:name="heading 3"/>
                    <w:lsdException w:qFormat="1" w:uiPriority="9" w:name="heading 4"/>
                    <w:lsdException w:qFormat="1" w:uiPriority="9" w:name="heading 5"/>
                    <w:lsdException w:qFormat="1" w:uiPriority="9" w:name="heading 6"/>
                    <w:lsdException w:qFormat="1" w:uiPriority="9" w:name="heading 7"/>
                    <w:lsdException w:qFormat="1" w:uiPriority="9" w:name="heading 8"/>
                    <w:lsdException w:qFormat="1" w:uiPriority="9" w:name="heading 9"/>
                    <w:lsdException w:uiPriority="99" w:name="index 1"/>
                    <w:lsdException w:uiPriority="99" w:name="index 2"/>
                    <w:lsdException w:uiPriority="99" w:name="index 3"/>
                    <w:lsdException w:uiPriority="99" w:name="index 4"/>
                    <w:lsdException w:uiPriority="99" w:name="index 5"/>
                    <w:lsdException w:uiPriority="99" w:name="index 6"/>
                    <w:lsdException w:uiPriority="99" w:name="index 7"/>
                    <w:lsdException w:uiPriority="99" w:name="index 8"/>
                    <w:lsdException w:uiPriority="99" w:name="index 9"/>
                    <w:lsdException w:uiPriority="39" w:name="toc 1"/>
                    <w:lsdException w:uiPriority="39" w:name="toc 2"/>
                    <w:lsdException w:uiPriority="39" w:name="toc 3"/>
                    <w:lsdException w:uiPriority="39" w:name="toc 4"/>
                    <w:lsdException w:uiPriority="39" w:name="toc 5"/>
                    <w:lsdException w:uiPriority="39" w:name="toc 6"/>
                    <w:lsdException w:uiPriority="39" w:name="toc 7"/>
                    <w:lsdException w:uiPriority="39" w:name="toc 8"/>
                    <w:lsdException w:uiPriority="39" w:name="toc 9"/>
                    <w:lsdException w:uiPriority="99" w:name="Normal Indent"/>
                    <w:lsdException w:uiPriority="99" w:name="footnote text"/>
                    <w:lsdException w:uiPriority="99" w:name="annotation text"/>
                    <w:lsdException w:qFormat="1" w:uiPriority="99" w:name="header"/>
                    <w:lsdException w:qFormat="1" w:uiPriority="99" w:name="footer"/>
                    <w:lsdException w:uiPriority="99" w:name="index heading"/>
                    <w:lsdException w:qFormat="1" w:uiPriority="35" w:name="caption"/>
                    <w:lsdException w:uiPriority="99" w:name="table of figures"/>
                    <w:lsdException w:uiPriority="99" w:name="envelope address"/>
                    <w:lsdException w:uiPriority="99" w:name="envelope return"/>
                    <w:lsdException w:uiPriority="99" w:name="footnote reference"/>
                    <w:lsdException w:uiPriority="99" w:name="annotation reference"/>
                    <w:lsdException w:uiPriority="99" w:name="line number"/>
                    <w:lsdException w:uiPriority="99" w:name="page number"/>
                    <w:lsdException w:uiPriority="99" w:name="endnote reference"/>
                    <w:lsdException w:uiPriority="99" w:name="endnote text"/>
                    <w:lsdException w:uiPriority="99" w:name="table of authorities"/>
                    <w:lsdException w:uiPriority="99" w:name="macro"/>
                    <w:lsdException w:uiPriority="99" w:name="toa heading"/>
                    <w:lsdException w:uiPriority="99" w:name="List"/>
                    <w:lsdException w:uiPriority="99" w:name="List Bullet"/>
                    <w:lsdException w:uiPriority="99" w:name="List Number"/>
                    <w:lsdException w:uiPriority="99" w:name="List 2"/>
                    <w:lsdException w:uiPriority="99" w:name="List 3"/>
                    <w:lsdException w:uiPriority="99" w:name="List 4"/>
                    <w:lsdException w:uiPriority="99" w:name="List 5"/>
                    <w:lsdException w:uiPriority="99" w:name="List Bullet 2"/>
                    <w:lsdException w:uiPriority="99" w:name="List Bullet 3"/>
                    <w:lsdException w:uiPriority="99" w:name="List Bullet 4"/>
                    <w:lsdException w:uiPriority="99" w:name="List Bullet 5"/>
                    <w:lsdException w:uiPriority="99" w:name="List Number 2"/>
                    <w:lsdException w:uiPriority="99" w:name="List Number 3"/>
                    <w:lsdException w:uiPriority="99" w:name="List Number 4"/>
                    <w:lsdException w:uiPriority="99" w:name="List Number 5"/>
                    <w:lsdException w:qFormat="1" w:unhideWhenUsed="0" w:uiPriority="10" w:semiHidden="0"
                                    w:name="Title"/>
                    <w:lsdException w:uiPriority="99" w:name="Closing"/>
                    <w:lsdException w:uiPriority="99" w:name="Signature"/>
                    <w:lsdException w:qFormat="1" w:uiPriority="1" w:name="Default Paragraph Font"/>
                    <w:lsdException w:uiPriority="99" w:name="Body Text"/>
                    <w:lsdException w:uiPriority="99" w:name="Body Text Indent"/>
                    <w:lsdException w:uiPriority="99" w:name="List Continue"/>
                    <w:lsdException w:uiPriority="99" w:name="List Continue 2"/>
                    <w:lsdException w:uiPriority="99" w:name="List Continue 3"/>
                    <w:lsdException w:uiPriority="99" w:name="List Continue 4"/>
                    <w:lsdException w:uiPriority="99" w:name="List Continue 5"/>
                    <w:lsdException w:uiPriority="99" w:name="Message Header"/>
                    <w:lsdException w:qFormat="1" w:unhideWhenUsed="0" w:uiPriority="11" w:semiHidden="0"
                                    w:name="Subtitle"/>
                    <w:lsdException w:uiPriority="99" w:name="Salutation"/>
                    <w:lsdException w:uiPriority="99" w:name="Date"/>
                    <w:lsdException w:uiPriority="99" w:name="Body Text First Indent"/>
                    <w:lsdException w:uiPriority="99" w:name="Body Text First Indent 2"/>
                    <w:lsdException w:uiPriority="99" w:name="Note Heading"/>
                    <w:lsdException w:uiPriority="99" w:name="Body Text 2"/>
                    <w:lsdException w:uiPriority="99" w:name="Body Text 3"/>
                    <w:lsdException w:uiPriority="99" w:name="Body Text Indent 2"/>
                    <w:lsdException w:uiPriority="99" w:name="Body Text Indent 3"/>
                    <w:lsdException w:uiPriority="99" w:name="Block Text"/>
                    <w:lsdException w:uiPriority="99" w:name="Hyperlink"/>
                    <w:lsdException w:uiPriority="99" w:name="FollowedHyperlink"/>
                    <w:lsdException w:qFormat="1" w:unhideWhenUsed="0" w:uiPriority="22" w:semiHidden="0"
                                    w:name="Strong"/>
                    <w:lsdException w:qFormat="1" w:unhideWhenUsed="0" w:uiPriority="20" w:semiHidden="0"
                                    w:name="Emphasis"/>
                    <w:lsdException w:uiPriority="99" w:name="Document Map"/>
                    <w:lsdException w:uiPriority="99" w:name="Plain Text"/>
                    <w:lsdException w:uiPriority="99" w:name="E-mail Signature"/>
                    <w:lsdException w:qFormat="1" w:uiPriority="99" w:name="Normal (Web)"/>
                    <w:lsdException w:uiPriority="99" w:name="HTML Acronym"/>
                    <w:lsdException w:uiPriority="99" w:name="HTML Address"/>
                    <w:lsdException w:uiPriority="99" w:name="HTML Cite"/>
                    <w:lsdException w:uiPriority="99" w:name="HTML Code"/>
                    <w:lsdException w:uiPriority="99" w:name="HTML Definition"/>
                    <w:lsdException w:uiPriority="99" w:name="HTML Keyboard"/>
                    <w:lsdException w:uiPriority="99" w:name="HTML Preformatted"/>
                    <w:lsdException w:uiPriority="99" w:name="HTML Sample"/>
                    <w:lsdException w:uiPriority="99" w:name="HTML Typewriter"/>
                    <w:lsdException w:uiPriority="99" w:name="HTML Variable"/>
                    <w:lsdException w:qFormat="1" w:uiPriority="99" w:name="Normal Table"/>
                    <w:lsdException w:uiPriority="99" w:name="annotation subject"/>
                    <w:lsdException w:uiPriority="99" w:name="Table Simple 1"/>
                    <w:lsdException w:uiPriority="99" w:name="Table Simple 2"/>
                    <w:lsdException w:uiPriority="99" w:name="Table Simple 3"/>
                    <w:lsdException w:uiPriority="99" w:name="Table Classic 1"/>
                    <w:lsdException w:uiPriority="99" w:name="Table Classic 2"/>
                    <w:lsdException w:uiPriority="99" w:name="Table Classic 3"/>
                    <w:lsdException w:uiPriority="99" w:name="Table Classic 4"/>
                    <w:lsdException w:uiPriority="99" w:name="Table Colorful 1"/>
                    <w:lsdException w:uiPriority="99" w:name="Table Colorful 2"/>
                    <w:lsdException w:uiPriority="99" w:name="Table Colorful 3"/>
                    <w:lsdException w:uiPriority="99" w:name="Table Columns 1"/>
                    <w:lsdException w:uiPriority="99" w:name="Table Columns 2"/>
                    <w:lsdException w:uiPriority="99" w:name="Table Columns 3"/>
                    <w:lsdException w:uiPriority="99" w:name="Table Columns 4"/>
                    <w:lsdException w:uiPriority="99" w:name="Table Columns 5"/>
                    <w:lsdException w:uiPriority="99" w:name="Table Grid 1"/>
                    <w:lsdException w:uiPriority="99" w:name="Table Grid 2"/>
                    <w:lsdException w:uiPriority="99" w:name="Table Grid 3"/>
                    <w:lsdException w:uiPriority="99" w:name="Table Grid 4"/>
                    <w:lsdException w:uiPriority="99" w:name="Table Grid 5"/>
                    <w:lsdException w:uiPriority="99" w:name="Table Grid 6"/>
                    <w:lsdException w:uiPriority="99" w:name="Table Grid 7"/>
                    <w:lsdException w:uiPriority="99" w:name="Table Grid 8"/>
                    <w:lsdException w:uiPriority="99" w:name="Table List 1"/>
                    <w:lsdException w:uiPriority="99" w:name="Table List 2"/>
                    <w:lsdException w:uiPriority="99" w:name="Table List 3"/>
                    <w:lsdException w:uiPriority="99" w:name="Table List 4"/>
                    <w:lsdException w:uiPriority="99" w:name="Table List 5"/>
                    <w:lsdException w:uiPriority="99" w:name="Table List 6"/>
                    <w:lsdException w:uiPriority="99" w:name="Table List 7"/>
                    <w:lsdException w:uiPriority="99" w:name="Table List 8"/>
                    <w:lsdException w:uiPriority="99" w:name="Table 3D effects 1"/>
                    <w:lsdException w:uiPriority="99" w:name="Table 3D effects 2"/>
                    <w:lsdException w:uiPriority="99" w:name="Table 3D effects 3"/>
                    <w:lsdException w:uiPriority="99" w:name="Table Contemporary"/>
                    <w:lsdException w:uiPriority="99" w:name="Table Elegant"/>
                    <w:lsdException w:uiPriority="99" w:name="Table Professional"/>
                    <w:lsdException w:uiPriority="99" w:name="Table Subtle 1"/>
                    <w:lsdException w:uiPriority="99" w:name="Table Subtle 2"/>
                    <w:lsdException w:uiPriority="99" w:name="Table Web 1"/>
                    <w:lsdException w:uiPriority="99" w:name="Table Web 2"/>
                    <w:lsdException w:uiPriority="99" w:name="Table Web 3"/>
                    <w:lsdException w:qFormat="1" w:uiPriority="99" w:name="Balloon Text"/>
                    <w:lsdException w:qFormat="1" w:unhideWhenUsed="0" w:uiPriority="59" w:semiHidden="0"
                                    w:name="Table Grid"/>
                    <w:lsdException w:uiPriority="99" w:name="Table Theme"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="60" w:semiHidden="0" w:name="Light Shading"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="61" w:semiHidden="0" w:name="Light List"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="62" w:semiHidden="0" w:name="Light Grid"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="63" w:semiHidden="0" w:name="Medium Shading 1"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="64" w:semiHidden="0" w:name="Medium Shading 2"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="65" w:semiHidden="0" w:name="Medium List 1"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="66" w:semiHidden="0" w:name="Medium List 2"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="67" w:semiHidden="0" w:name="Medium Grid 1"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="68" w:semiHidden="0" w:name="Medium Grid 2"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="69" w:semiHidden="0" w:name="Medium Grid 3"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="70" w:semiHidden="0" w:name="Dark List"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="71" w:semiHidden="0" w:name="Colorful Shading"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="72" w:semiHidden="0" w:name="Colorful List"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="73" w:semiHidden="0" w:name="Colorful Grid"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="60" w:semiHidden="0"
                                    w:name="Light Shading Accent 1"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="61" w:semiHidden="0"
                                    w:name="Light List Accent 1"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="62" w:semiHidden="0"
                                    w:name="Light Grid Accent 1"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="63" w:semiHidden="0"
                                    w:name="Medium Shading 1 Accent 1"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="64" w:semiHidden="0"
                                    w:name="Medium Shading 2 Accent 1"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="65" w:semiHidden="0"
                                    w:name="Medium List 1 Accent 1"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="66" w:semiHidden="0"
                                    w:name="Medium List 2 Accent 1"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="67" w:semiHidden="0"
                                    w:name="Medium Grid 1 Accent 1"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="68" w:semiHidden="0"
                                    w:name="Medium Grid 2 Accent 1"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="69" w:semiHidden="0"
                                    w:name="Medium Grid 3 Accent 1"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="70" w:semiHidden="0"
                                    w:name="Dark List Accent 1"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="71" w:semiHidden="0"
                                    w:name="Colorful Shading Accent 1"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="72" w:semiHidden="0"
                                    w:name="Colorful List Accent 1"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="73" w:semiHidden="0"
                                    w:name="Colorful Grid Accent 1"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="60" w:semiHidden="0"
                                    w:name="Light Shading Accent 2"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="61" w:semiHidden="0"
                                    w:name="Light List Accent 2"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="62" w:semiHidden="0"
                                    w:name="Light Grid Accent 2"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="63" w:semiHidden="0"
                                    w:name="Medium Shading 1 Accent 2"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="64" w:semiHidden="0"
                                    w:name="Medium Shading 2 Accent 2"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="65" w:semiHidden="0"
                                    w:name="Medium List 1 Accent 2"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="66" w:semiHidden="0"
                                    w:name="Medium List 2 Accent 2"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="67" w:semiHidden="0"
                                    w:name="Medium Grid 1 Accent 2"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="68" w:semiHidden="0"
                                    w:name="Medium Grid 2 Accent 2"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="69" w:semiHidden="0"
                                    w:name="Medium Grid 3 Accent 2"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="70" w:semiHidden="0"
                                    w:name="Dark List Accent 2"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="71" w:semiHidden="0"
                                    w:name="Colorful Shading Accent 2"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="72" w:semiHidden="0"
                                    w:name="Colorful List Accent 2"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="73" w:semiHidden="0"
                                    w:name="Colorful Grid Accent 2"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="60" w:semiHidden="0"
                                    w:name="Light Shading Accent 3"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="61" w:semiHidden="0"
                                    w:name="Light List Accent 3"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="62" w:semiHidden="0"
                                    w:name="Light Grid Accent 3"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="63" w:semiHidden="0"
                                    w:name="Medium Shading 1 Accent 3"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="64" w:semiHidden="0"
                                    w:name="Medium Shading 2 Accent 3"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="65" w:semiHidden="0"
                                    w:name="Medium List 1 Accent 3"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="66" w:semiHidden="0"
                                    w:name="Medium List 2 Accent 3"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="67" w:semiHidden="0"
                                    w:name="Medium Grid 1 Accent 3"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="68" w:semiHidden="0"
                                    w:name="Medium Grid 2 Accent 3"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="69" w:semiHidden="0"
                                    w:name="Medium Grid 3 Accent 3"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="70" w:semiHidden="0"
                                    w:name="Dark List Accent 3"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="71" w:semiHidden="0"
                                    w:name="Colorful Shading Accent 3"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="72" w:semiHidden="0"
                                    w:name="Colorful List Accent 3"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="73" w:semiHidden="0"
                                    w:name="Colorful Grid Accent 3"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="60" w:semiHidden="0"
                                    w:name="Light Shading Accent 4"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="61" w:semiHidden="0"
                                    w:name="Light List Accent 4"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="62" w:semiHidden="0"
                                    w:name="Light Grid Accent 4"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="63" w:semiHidden="0"
                                    w:name="Medium Shading 1 Accent 4"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="64" w:semiHidden="0"
                                    w:name="Medium Shading 2 Accent 4"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="65" w:semiHidden="0"
                                    w:name="Medium List 1 Accent 4"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="66" w:semiHidden="0"
                                    w:name="Medium List 2 Accent 4"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="67" w:semiHidden="0"
                                    w:name="Medium Grid 1 Accent 4"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="68" w:semiHidden="0"
                                    w:name="Medium Grid 2 Accent 4"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="69" w:semiHidden="0"
                                    w:name="Medium Grid 3 Accent 4"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="70" w:semiHidden="0"
                                    w:name="Dark List Accent 4"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="71" w:semiHidden="0"
                                    w:name="Colorful Shading Accent 4"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="72" w:semiHidden="0"
                                    w:name="Colorful List Accent 4"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="73" w:semiHidden="0"
                                    w:name="Colorful Grid Accent 4"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="60" w:semiHidden="0"
                                    w:name="Light Shading Accent 5"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="61" w:semiHidden="0"
                                    w:name="Light List Accent 5"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="62" w:semiHidden="0"
                                    w:name="Light Grid Accent 5"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="63" w:semiHidden="0"
                                    w:name="Medium Shading 1 Accent 5"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="64" w:semiHidden="0"
                                    w:name="Medium Shading 2 Accent 5"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="65" w:semiHidden="0"
                                    w:name="Medium List 1 Accent 5"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="66" w:semiHidden="0"
                                    w:name="Medium List 2 Accent 5"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="67" w:semiHidden="0"
                                    w:name="Medium Grid 1 Accent 5"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="68" w:semiHidden="0"
                                    w:name="Medium Grid 2 Accent 5"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="69" w:semiHidden="0"
                                    w:name="Medium Grid 3 Accent 5"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="70" w:semiHidden="0"
                                    w:name="Dark List Accent 5"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="71" w:semiHidden="0"
                                    w:name="Colorful Shading Accent 5"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="72" w:semiHidden="0"
                                    w:name="Colorful List Accent 5"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="73" w:semiHidden="0"
                                    w:name="Colorful Grid Accent 5"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="60" w:semiHidden="0"
                                    w:name="Light Shading Accent 6"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="61" w:semiHidden="0"
                                    w:name="Light List Accent 6"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="62" w:semiHidden="0"
                                    w:name="Light Grid Accent 6"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="63" w:semiHidden="0"
                                    w:name="Medium Shading 1 Accent 6"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="64" w:semiHidden="0"
                                    w:name="Medium Shading 2 Accent 6"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="65" w:semiHidden="0"
                                    w:name="Medium List 1 Accent 6"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="66" w:semiHidden="0"
                                    w:name="Medium List 2 Accent 6"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="67" w:semiHidden="0"
                                    w:name="Medium Grid 1 Accent 6"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="68" w:semiHidden="0"
                                    w:name="Medium Grid 2 Accent 6"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="69" w:semiHidden="0"
                                    w:name="Medium Grid 3 Accent 6"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="70" w:semiHidden="0"
                                    w:name="Dark List Accent 6"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="71" w:semiHidden="0"
                                    w:name="Colorful Shading Accent 6"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="72" w:semiHidden="0"
                                    w:name="Colorful List Accent 6"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="73" w:semiHidden="0"
                                    w:name="Colorful Grid Accent 6"/>
                </w:latentStyles>
                <w:style w:type="paragraph" w:default="1" w:styleId="1">
                    <w:name w:val="Normal"/>
                    <w:qFormat/>
                    <w:uiPriority w:val="0"/>
                    <w:pPr>
                        <w:widowControl w:val="0"/>
                        <w:jc w:val="both"/>
                    </w:pPr>
                    <w:rPr>
                        <w:rFonts w:asciiTheme="minorHAnsi" w:hAnsiTheme="minorHAnsi" w:eastAsiaTheme="minorEastAsia"
                                  w:cstheme="minorBidi"/>
                        <w:kern w:val="2"/>
                        <w:sz w:val="21"/>
                        <w:szCs w:val="22"/>
                        <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="paragraph" w:styleId="2">
                    <w:name w:val="heading 2"/>
                    <w:basedOn w:val="1"/>
                    <w:next w:val="1"/>
                    <w:semiHidden/>
                    <w:unhideWhenUsed/>
                    <w:qFormat/>
                    <w:uiPriority w:val="9"/>
                    <w:pPr>
                        <w:spacing w:before="0" w:beforeAutospacing="1" w:after="0" w:afterAutospacing="1"/>
                        <w:jc w:val="left"/>
                    </w:pPr>
                    <w:rPr>
                        <w:rFonts w:hint="eastAsia" w:ascii="宋体" w:hAnsi="宋体" w:eastAsia="宋体" w:cs="宋体"/>
                        <w:b/>
                        <w:kern w:val="0"/>
                        <w:sz w:val="36"/>
                        <w:szCs w:val="36"/>
                        <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="character" w:default="1" w:styleId="9">
                    <w:name w:val="Default Paragraph Font"/>
                    <w:semiHidden/>
                    <w:unhideWhenUsed/>
                    <w:qFormat/>
                    <w:uiPriority w:val="1"/>
                </w:style>
                <w:style w:type="table" w:default="1" w:styleId="7">
                    <w:name w:val="Normal Table"/>
                    <w:semiHidden/>
                    <w:unhideWhenUsed/>
                    <w:qFormat/>
                    <w:uiPriority w:val="99"/>
                    <w:tblPr>
                        <w:tblCellMar>
                            <w:top w:w="0" w:type="dxa"/>
                            <w:left w:w="108" w:type="dxa"/>
                            <w:bottom w:w="0" w:type="dxa"/>
                            <w:right w:w="108" w:type="dxa"/>
                        </w:tblCellMar>
                    </w:tblPr>
                </w:style>
                <w:style w:type="paragraph" w:styleId="3">
                    <w:name w:val="Balloon Text"/>
                    <w:basedOn w:val="1"/>
                    <w:link w:val="10"/>
                    <w:semiHidden/>
                    <w:unhideWhenUsed/>
                    <w:qFormat/>
                    <w:uiPriority w:val="99"/>
                    <w:rPr>
                        <w:sz w:val="18"/>
                        <w:szCs w:val="18"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="paragraph" w:styleId="4">
                    <w:name w:val="footer"/>
                    <w:basedOn w:val="1"/>
                    <w:link w:val="12"/>
                    <w:semiHidden/>
                    <w:unhideWhenUsed/>
                    <w:qFormat/>
                    <w:uiPriority w:val="99"/>
                    <w:pPr>
                        <w:tabs>
                            <w:tab w:val="center" w:pos="4153"/>
                            <w:tab w:val="right" w:pos="8306"/>
                        </w:tabs>
                        <w:snapToGrid w:val="0"/>
                        <w:jc w:val="left"/>
                    </w:pPr>
                    <w:rPr>
                        <w:sz w:val="18"/>
                        <w:szCs w:val="18"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="paragraph" w:styleId="5">
                    <w:name w:val="header"/>
                    <w:basedOn w:val="1"/>
                    <w:link w:val="11"/>
                    <w:semiHidden/>
                    <w:unhideWhenUsed/>
                    <w:qFormat/>
                    <w:uiPriority w:val="99"/>
                    <w:pPr>
                        <w:pBdr>
                            <w:bottom w:val="single" w:color="auto" w:sz="6" w:space="1"/>
                        </w:pBdr>
                        <w:tabs>
                            <w:tab w:val="center" w:pos="4153"/>
                            <w:tab w:val="right" w:pos="8306"/>
                        </w:tabs>
                        <w:snapToGrid w:val="0"/>
                        <w:jc w:val="center"/>
                    </w:pPr>
                    <w:rPr>
                        <w:sz w:val="18"/>
                        <w:szCs w:val="18"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="paragraph" w:styleId="6">
                    <w:name w:val="Normal (Web)"/>
                    <w:basedOn w:val="1"/>
                    <w:semiHidden/>
                    <w:unhideWhenUsed/>
                    <w:qFormat/>
                    <w:uiPriority w:val="99"/>
                    <w:pPr>
                        <w:spacing w:before="0" w:beforeAutospacing="1" w:after="0" w:afterAutospacing="1"/>
                        <w:ind w:left="0" w:right="0"/>
                        <w:jc w:val="left"/>
                    </w:pPr>
                    <w:rPr>
                        <w:kern w:val="0"/>
                        <w:sz w:val="24"/>
                        <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="table" w:styleId="8">
                    <w:name w:val="Table Grid"/>
                    <w:basedOn w:val="7"/>
                    <w:qFormat/>
                    <w:uiPriority w:val="59"/>
                    <w:tblPr>
                        <w:tblBorders>
                            <w:top w:val="single" w:color="000000" w:themeColor="text1" w:sz="4" w:space="0"/>
                            <w:left w:val="single" w:color="000000" w:themeColor="text1" w:sz="4" w:space="0"/>
                            <w:bottom w:val="single" w:color="000000" w:themeColor="text1" w:sz="4" w:space="0"/>
                            <w:right w:val="single" w:color="000000" w:themeColor="text1" w:sz="4" w:space="0"/>
                            <w:insideH w:val="single" w:color="000000" w:themeColor="text1" w:sz="4" w:space="0"/>
                            <w:insideV w:val="single" w:color="000000" w:themeColor="text1" w:sz="4" w:space="0"/>
                        </w:tblBorders>
                        <w:tblCellMar>
                            <w:top w:w="0" w:type="dxa"/>
                            <w:left w:w="108" w:type="dxa"/>
                            <w:bottom w:w="0" w:type="dxa"/>
                            <w:right w:w="108" w:type="dxa"/>
                        </w:tblCellMar>
                    </w:tblPr>
                </w:style>
                <w:style w:type="character" w:customStyle="1" w:styleId="10">
                    <w:name w:val="批注框文本 Char"/>
                    <w:basedOn w:val="9"/>
                    <w:link w:val="3"/>
                    <w:semiHidden/>
                    <w:qFormat/>
                    <w:uiPriority w:val="99"/>
                    <w:rPr>
                        <w:sz w:val="18"/>
                        <w:szCs w:val="18"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="character" w:customStyle="1" w:styleId="11">
                    <w:name w:val="页眉 Char"/>
                    <w:basedOn w:val="9"/>
                    <w:link w:val="5"/>
                    <w:semiHidden/>
                    <w:qFormat/>
                    <w:uiPriority w:val="99"/>
                    <w:rPr>
                        <w:sz w:val="18"/>
                        <w:szCs w:val="18"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="character" w:customStyle="1" w:styleId="12">
                    <w:name w:val="页脚 Char"/>
                    <w:basedOn w:val="9"/>
                    <w:link w:val="4"/>
                    <w:semiHidden/>
                    <w:qFormat/>
                    <w:uiPriority w:val="99"/>
                    <w:rPr>
                        <w:sz w:val="18"/>
                        <w:szCs w:val="18"/>
                    </w:rPr>
                </w:style>
            </w:styles>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/word/theme/theme1.xml"
              pkg:contentType="application/vnd.openxmlformats-officedocument.theme+xml">
        <pkg:xmlData>
            <a:theme xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main" name="Office 主题">
                <a:themeElements>
                    <a:clrScheme name="Office">
                        <a:dk1>
                            <a:sysClr val="windowText" lastClr="000000"/>
                        </a:dk1>
                        <a:lt1>
                            <a:sysClr val="window" lastClr="FFFFFF"/>
                        </a:lt1>
                        <a:dk2>
                            <a:srgbClr val="1F497D"/>
                        </a:dk2>
                        <a:lt2>
                            <a:srgbClr val="EEECE1"/>
                        </a:lt2>
                        <a:accent1>
                            <a:srgbClr val="4F81BD"/>
                        </a:accent1>
                        <a:accent2>
                            <a:srgbClr val="C0504D"/>
                        </a:accent2>
                        <a:accent3>
                            <a:srgbClr val="9BBB59"/>
                        </a:accent3>
                        <a:accent4>
                            <a:srgbClr val="8064A2"/>
                        </a:accent4>
                        <a:accent5>
                            <a:srgbClr val="4BACC6"/>
                        </a:accent5>
                        <a:accent6>
                            <a:srgbClr val="F79646"/>
                        </a:accent6>
                        <a:hlink>
                            <a:srgbClr val="0000FF"/>
                        </a:hlink>
                        <a:folHlink>
                            <a:srgbClr val="800080"/>
                        </a:folHlink>
                    </a:clrScheme>
                    <a:fontScheme name="Office">
                        <a:majorFont>
                            <a:latin typeface="Cambria"/>
                            <a:ea typeface=""/>
                            <a:cs typeface=""/>
                            <a:font script="Jpan" typeface="ＭＳ ゴシック"/>
                            <a:font script="Hang" typeface="맑은 고딕"/>
                            <a:font script="Hans" typeface="宋体"/>
                            <a:font script="Hant" typeface="新細明體"/>
                            <a:font script="Arab" typeface="Times New Roman"/>
                            <a:font script="Hebr" typeface="Times New Roman"/>
                            <a:font script="Thai" typeface="Angsana New"/>
                            <a:font script="Ethi" typeface="Nyala"/>
                            <a:font script="Beng" typeface="Vrinda"/>
                            <a:font script="Gujr" typeface="Shruti"/>
                            <a:font script="Khmr" typeface="MoolBoran"/>
                            <a:font script="Knda" typeface="Tunga"/>
                            <a:font script="Guru" typeface="Raavi"/>
                            <a:font script="Cans" typeface="Euphemia"/>
                            <a:font script="Cher" typeface="Plantagenet Cherokee"/>
                            <a:font script="Yiii" typeface="Microsoft Yi Baiti"/>
                            <a:font script="Tibt" typeface="Microsoft Himalaya"/>
                            <a:font script="Thaa" typeface="MV Boli"/>
                            <a:font script="Deva" typeface="Mangal"/>
                            <a:font script="Telu" typeface="Gautami"/>
                            <a:font script="Taml" typeface="Latha"/>
                            <a:font script="Syrc" typeface="Estrangelo Edessa"/>
                            <a:font script="Orya" typeface="Kalinga"/>
                            <a:font script="Mlym" typeface="Kartika"/>
                            <a:font script="Laoo" typeface="DokChampa"/>
                            <a:font script="Sinh" typeface="Iskoola Pota"/>
                            <a:font script="Mong" typeface="Mongolian Baiti"/>
                            <a:font script="Viet" typeface="Times New Roman"/>
                            <a:font script="Uigh" typeface="Microsoft Uighur"/>
                        </a:majorFont>
                        <a:minorFont>
                            <a:latin typeface="Calibri"/>
                            <a:ea typeface=""/>
                            <a:cs typeface=""/>
                            <a:font script="Jpan" typeface="ＭＳ 明朝"/>
                            <a:font script="Hang" typeface="맑은 고딕"/>
                            <a:font script="Hans" typeface="宋体"/>
                            <a:font script="Hant" typeface="新細明體"/>
                            <a:font script="Arab" typeface="Arial"/>
                            <a:font script="Hebr" typeface="Arial"/>
                            <a:font script="Thai" typeface="Cordia New"/>
                            <a:font script="Ethi" typeface="Nyala"/>
                            <a:font script="Beng" typeface="Vrinda"/>
                            <a:font script="Gujr" typeface="Shruti"/>
                            <a:font script="Khmr" typeface="DaunPenh"/>
                            <a:font script="Knda" typeface="Tunga"/>
                            <a:font script="Guru" typeface="Raavi"/>
                            <a:font script="Cans" typeface="Euphemia"/>
                            <a:font script="Cher" typeface="Plantagenet Cherokee"/>
                            <a:font script="Yiii" typeface="Microsoft Yi Baiti"/>
                            <a:font script="Tibt" typeface="Microsoft Himalaya"/>
                            <a:font script="Thaa" typeface="MV Boli"/>
                            <a:font script="Deva" typeface="Mangal"/>
                            <a:font script="Telu" typeface="Gautami"/>
                            <a:font script="Taml" typeface="Latha"/>
                            <a:font script="Syrc" typeface="Estrangelo Edessa"/>
                            <a:font script="Orya" typeface="Kalinga"/>
                            <a:font script="Mlym" typeface="Kartika"/>
                            <a:font script="Laoo" typeface="DokChampa"/>
                            <a:font script="Sinh" typeface="Iskoola Pota"/>
                            <a:font script="Mong" typeface="Mongolian Baiti"/>
                            <a:font script="Viet" typeface="Arial"/>
                            <a:font script="Uigh" typeface="Microsoft Uighur"/>
                        </a:minorFont>
                    </a:fontScheme>
                    <a:fmtScheme name="Office">
                        <a:fillStyleLst>
                            <a:solidFill>
                                <a:schemeClr val="phClr"/>
                            </a:solidFill>
                            <a:gradFill rotWithShape="1">
                                <a:gsLst>
                                    <a:gs pos="0">
                                        <a:schemeClr val="phClr">
                                            <a:tint val="50000"/>
                                            <a:satMod val="300000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                    <a:gs pos="35000">
                                        <a:schemeClr val="phClr">
                                            <a:tint val="37000"/>
                                            <a:satMod val="300000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                    <a:gs pos="100000">
                                        <a:schemeClr val="phClr">
                                            <a:tint val="15000"/>
                                            <a:satMod val="350000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                </a:gsLst>
                                <a:lin ang="16200000" scaled="1"/>
                            </a:gradFill>
                            <a:gradFill rotWithShape="1">
                                <a:gsLst>
                                    <a:gs pos="0">
                                        <a:schemeClr val="phClr">
                                            <a:shade val="51000"/>
                                            <a:satMod val="130000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                    <a:gs pos="80000">
                                        <a:schemeClr val="phClr">
                                            <a:shade val="93000"/>
                                            <a:satMod val="130000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                    <a:gs pos="100000">
                                        <a:schemeClr val="phClr">
                                            <a:shade val="94000"/>
                                            <a:satMod val="135000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                </a:gsLst>
                                <a:lin ang="16200000" scaled="0"/>
                            </a:gradFill>
                        </a:fillStyleLst>
                        <a:lnStyleLst>
                            <a:ln w="9525" cap="flat" cmpd="sng" algn="ctr">
                                <a:solidFill>
                                    <a:schemeClr val="phClr">
                                        <a:shade val="95000"/>
                                        <a:satMod val="105000"/>
                                    </a:schemeClr>
                                </a:solidFill>
                                <a:prstDash val="solid"/>
                            </a:ln>
                            <a:ln w="25400" cap="flat" cmpd="sng" algn="ctr">
                                <a:solidFill>
                                    <a:schemeClr val="phClr"/>
                                </a:solidFill>
                                <a:prstDash val="solid"/>
                            </a:ln>
                            <a:ln w="38100" cap="flat" cmpd="sng" algn="ctr">
                                <a:solidFill>
                                    <a:schemeClr val="phClr"/>
                                </a:solidFill>
                                <a:prstDash val="solid"/>
                            </a:ln>
                        </a:lnStyleLst>
                        <a:effectStyleLst>
                            <a:effectStyle>
                                <a:effectLst>
                                    <a:outerShdw blurRad="40000" dist="20000" dir="5400000" rotWithShape="0">
                                        <a:srgbClr val="000000">
                                            <a:alpha val="38000"/>
                                        </a:srgbClr>
                                    </a:outerShdw>
                                </a:effectLst>
                            </a:effectStyle>
                            <a:effectStyle>
                                <a:effectLst>
                                    <a:outerShdw blurRad="40000" dist="23000" dir="5400000" rotWithShape="0">
                                        <a:srgbClr val="000000">
                                            <a:alpha val="35000"/>
                                        </a:srgbClr>
                                    </a:outerShdw>
                                </a:effectLst>
                            </a:effectStyle>
                            <a:effectStyle>
                                <a:effectLst>
                                    <a:outerShdw blurRad="40000" dist="23000" dir="5400000" rotWithShape="0">
                                        <a:srgbClr val="000000">
                                            <a:alpha val="35000"/>
                                        </a:srgbClr>
                                    </a:outerShdw>
                                </a:effectLst>
                                <a:scene3d>
                                    <a:camera prst="orthographicFront">
                                        <a:rot lat="0" lon="0" rev="0"/>
                                    </a:camera>
                                    <a:lightRig rig="threePt" dir="t">
                                        <a:rot lat="0" lon="0" rev="1200000"/>
                                    </a:lightRig>
                                </a:scene3d>
                                <a:sp3d>
                                    <a:bevelT w="63500" h="25400"/>
                                </a:sp3d>
                            </a:effectStyle>
                        </a:effectStyleLst>
                        <a:bgFillStyleLst>
                            <a:solidFill>
                                <a:schemeClr val="phClr"/>
                            </a:solidFill>
                            <a:gradFill rotWithShape="1">
                                <a:gsLst>
                                    <a:gs pos="0">
                                        <a:schemeClr val="phClr">
                                            <a:tint val="40000"/>
                                            <a:satMod val="350000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                    <a:gs pos="40000">
                                        <a:schemeClr val="phClr">
                                            <a:tint val="45000"/>
                                            <a:shade val="99000"/>
                                            <a:satMod val="350000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                    <a:gs pos="100000">
                                        <a:schemeClr val="phClr">
                                            <a:shade val="20000"/>
                                            <a:satMod val="255000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                </a:gsLst>
                                <a:path path="circle">
                                    <a:fillToRect l="50000" t="-80000" r="50000" b="180000"/>
                                </a:path>
                            </a:gradFill>
                            <a:gradFill rotWithShape="1">
                                <a:gsLst>
                                    <a:gs pos="0">
                                        <a:schemeClr val="phClr">
                                            <a:tint val="80000"/>
                                            <a:satMod val="300000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                    <a:gs pos="100000">
                                        <a:schemeClr val="phClr">
                                            <a:shade val="30000"/>
                                            <a:satMod val="200000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                </a:gsLst>
                                <a:path path="circle">
                                    <a:fillToRect l="50000" t="50000" r="50000" b="50000"/>
                                </a:path>
                            </a:gradFill>
                        </a:bgFillStyleLst>
                    </a:fmtScheme>
                </a:themeElements>
                <a:objectDefaults/>
            </a:theme>
        </pkg:xmlData>
    </pkg:part>
</pkg:package>