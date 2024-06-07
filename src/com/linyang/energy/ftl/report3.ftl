<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<?mso-application progid="Word.Document"?>
<pkg:package xmlns:pkg="http://schemas.microsoft.com/office/2006/xmlPackage">
    <pkg:part pkg:name="/_rels/.rels" pkg:contentType="application/vnd.openxmlformats-package.relationships+xml">
        <pkg:xmlData>
            <Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
                <Relationship Id="rId4"
                              Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument"
                              Target="word/document.xml"/>
                <Relationship Id="rId2"
                              Type="http://schemas.openxmlformats.org/package/2006/relationships/metadata/core-properties"
                              Target="docProps/core.xml"/>
                <Relationship Id="rId1"
                              Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/extended-properties"
                              Target="docProps/app.xml"/>
                <Relationship Id="rId3"
                              Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/custom-properties"
                              Target="docProps/custom.xml"/>
            </Relationships>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/word/_rels/document.xml.rels"
              pkg:contentType="application/vnd.openxmlformats-package.relationships+xml">
        <pkg:xmlData>
            <Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
            <#-- 日电量曲线 -->
                <Relationship Id="rId4" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image"
                              Target="media/image1.png"/>
            <#-- 本月电量 -->
                <Relationship Id="rId5" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image"
                              Target="media/image2.png"/>
            <#-- 本月电费 -->
                <Relationship Id="rId6" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image"
                              Target="media/image3.png"/>
            <#-- 有功功率曲线 -->
                <Relationship Id="rId7" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image"
                              Target="media/image4.png"/>

            <#--<Relationship Id="rId8" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="media/image5.png"/>-->
            <#-- 谐波电流 -->
                <#list table3 as harPlan>
                    <Relationship Id="${harPlan.rId}"
                                  Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image"
                                  Target="media/${harPlan.image}.png"/>
                </#list>


            <#--<Relationship Id="rId9" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="media/image6.png"/>-->
            <#-- 谐波电压 -->
                <#list table4 as harVPlan>
                    <Relationship Id="${harVPlan.rId}"
                                  Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image"
                                  Target="media/${harVPlan.image}.png"/>
                </#list>

            <#--<Relationship Id="rId10" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="media/image7.png"/>-->
            <#-- 企业设备能效图片 -->
                <Relationship Id="${chartEERid}"
                              Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image"
                              Target="media/${chartEEImage}.png"/>

            <#--<Relationship Id="rId18" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="media/image15.png"/>-->
            <#--<Relationship Id="rId17" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="media/image14.png"/>-->
            <#--<Relationship Id="rId16" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="media/image13.png"/>-->
            <#--<Relationship Id="rId15" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="media/image12.png"/>-->
            <#--<Relationship Id="rId14" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="media/image11.png"/>-->
            <#--<Relationship Id="rId13" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="media/image10.png"/>-->
            <#--<Relationship Id="rId12" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="media/image9.png"/>-->
            <#--<Relationship Id="rId11" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="media/image8.png"/>-->
                <#list canUseParam as canUse>
                    <Relationship Id="${canUse.maxTimerId}"
                                  Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image"
                                  Target="media/${canUse.maxTimeimage}.png"/>
                    <Relationship Id="${canUse.maxLoadrId}"
                                  Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image"
                                  Target="media/${canUse.maxLoadimage}.png"/>
                    <Relationship Id="${canUse.maxIToprId}"
                                  Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image"
                                  Target="media/${canUse.maxITopimage}.png"/>
                    <Relationship Id="${canUse.maxItimerId}"
                                  Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image"
                                  Target="media/${canUse.maxItimeimage}.png"/>
                    <Relationship Id="${canUse.sumItimerId}"
                                  Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image"
                                  Target="media/${canUse.sumItimeimage}.png"/>
                    <Relationship Id="${canUse.maxVtoprId}"
                                  Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image"
                                  Target="media/${canUse.maxVtopimage}.png"/>
                    <Relationship Id="${canUse.maxVtimerId}"
                                  Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image"
                                  Target="media/${canUse.maxVtimeimage}.png"/>
                    <Relationship Id="${canUse.sumVtimerId}"
                                  Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image"
                                  Target="media/${canUse.sumVtimeimage}.png"/>
                </#list>

                <Relationship Id="rId3" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/theme"
                              Target="theme/theme1.xml"/>
                <Relationship Id="rId21"
                              Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/fontTable"
                              Target="fontTable.xml"/>
                <Relationship Id="rId20"
                              Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/numbering"
                              Target="numbering.xml"/>
                <Relationship Id="rId2"
                              Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/settings"
                              Target="settings.xml"/>
                <Relationship Id="rId19"
                              Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/customXml"
                              Target="../customXml/item1.xml"/>
                <Relationship Id="rId1"
                              Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles"
                              Target="styles.xml"/>
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
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="48"/>
                                <w:szCs w:val="48"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="48"/>
                                <w:szCs w:val="48"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>企业用能分析报告</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="48"/>
                                <w:szCs w:val="48"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="left"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="36"/>
                                <w:szCs w:val="36"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="36"/>
                                <w:szCs w:val="36"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>企业名称：${ledgerName}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="left"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="36"/>
                                <w:szCs w:val="36"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="36"/>
                                <w:szCs w:val="36"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>分析周期：</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="default" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="36"/>
                                <w:szCs w:val="36"/>
                                <w:lang w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>${startDate}</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="36"/>
                                <w:szCs w:val="36"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>至</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="default" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="36"/>
                                <w:szCs w:val="36"/>
                                <w:lang w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>${endDate}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="48"/>
                                <w:szCs w:val="48"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="36"/>
                                <w:szCs w:val="36"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="36"/>
                                <w:szCs w:val="36"/>
                                <w:lang w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t xml:space="preserve">1. </w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="36"/>
                                <w:szCs w:val="36"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>企业用能分析</w:t>
                        </w:r>
                        <w:bookmarkStart w:id="0" w:name="_GoBack"/>
                        <w:bookmarkEnd w:id="0"/>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="32"/>
                                <w:szCs w:val="32"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="32"/>
                                <w:szCs w:val="32"/>
                                <w:lang w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t xml:space="preserve">1.1 </w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="32"/>
                                <w:szCs w:val="32"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>用能概况</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="both"/>
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
                                    <wp:effectExtent l="0" t="0" r="8890" b="13970"/>
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
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:color w:val="auto"/>
                                <w:sz w:val="21"/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="auto"/>
                                <w:sz w:val="21"/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>日电量曲线</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="8497B0" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="13"/>
                                <w:szCs w:val="13"/>
                                <w14:textFill>
                                    <w14:solidFill>
                                        <w14:schemeClr w14:val="tx2">
                                            <w14:lumMod w14:val="60000"/>
                                            <w14:lumOff w14:val="40000"/>
                                        </w14:schemeClr>
                                    </w14:solidFill>
                                </w14:textFill>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:color w:val="8497B0" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="13"/>
                                <w:szCs w:val="13"/>
                                <w14:textFill>
                                    <w14:solidFill>
                                        <w14:schemeClr w14:val="tx2">
                                            <w14:lumMod w14:val="60000"/>
                                            <w14:lumOff w14:val="40000"/>
                                        </w14:schemeClr>
                                    </w14:solidFill>
                                </w14:textFill>
                            </w:rPr>
                            <w:drawing>
                                <wp:inline distT="0" distB="0" distL="0" distR="0">
                                    <wp:extent cx="2545080" cy="1249045"/>
                                    <wp:effectExtent l="0" t="0" r="20320" b="20955"/>
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
                                <w:color w:val="8497B0" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="13"/>
                                <w:szCs w:val="13"/>
                                <w14:textFill>
                                    <w14:solidFill>
                                        <w14:schemeClr w14:val="tx2">
                                            <w14:lumMod w14:val="60000"/>
                                            <w14:lumOff w14:val="40000"/>
                                        </w14:schemeClr>
                                    </w14:solidFill>
                                </w14:textFill>
                            </w:rPr>
                            <w:drawing>
                                <wp:inline distT="0" distB="0" distL="0" distR="0">
                                    <wp:extent cx="2545715" cy="1268730"/>
                                    <wp:effectExtent l="0" t="0" r="19685" b="1270"/>
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
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="8497B0" w:themeColor="text2" w:themeTint="99"/>
                                <w:sz w:val="13"/>
                                <w:szCs w:val="13"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                                <w14:textFill>
                                    <w14:solidFill>
                                        <w14:schemeClr w14:val="tx2">
                                            <w14:lumMod w14:val="60000"/>
                                            <w14:lumOff w14:val="40000"/>
                                        </w14:schemeClr>
                                    </w14:solidFill>
                                </w14:textFill>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="auto"/>
                                <w:sz w:val="21"/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>电量</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:color w:val="auto"/>
                                <w:sz w:val="21"/>
                                <w:szCs w:val="21"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>电费</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:ind w:firstLine="420" w:firstLineChars="0"/>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>企业分析周期内用能情况如下：</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:ind w:firstLine="420" w:firstLineChars="0"/>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>总用电量：</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:color w:val="0000FF"/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>${allEle} kWh</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:ind w:firstLine="420" w:firstLineChars="0"/>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="default" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>尖段电量占比：</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="default" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:color w:val="0000FF"/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>${ele1}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:ind w:firstLine="420" w:firstLineChars="0"/>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>峰段电量占：</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:color w:val="0000FF"/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>${ele2}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:ind w:firstLine="420" w:firstLineChars="0"/>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>平段电量占：</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:color w:val="0000FF"/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>${ele3}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:ind w:firstLine="420" w:firstLineChars="0"/>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:color w:val="0000FF"/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>谷段电量占：</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:color w:val="0000FF"/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>${ele4}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="32"/>
                                <w:szCs w:val="32"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="default" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="32"/>
                                <w:szCs w:val="32"/>
                                <w:lang w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t xml:space="preserve">1.2 </w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="32"/>
                                <w:szCs w:val="32"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>电能质量</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="default" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                                <w:lang w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t xml:space="preserve">1.2.1 </w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>用电负荷</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:ind w:firstLine="420" w:firstLineChars="0"/>
                            <w:jc w:val="left"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:hAnsi="Times New Roman" w:cs="Times New Roman"/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                            </w:rPr>
                            <w:t>${powerPText}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="both"/>
                        </w:pPr>
                        <w:r>
                            <w:drawing>
                                <wp:inline distT="0" distB="0" distL="114300" distR="114300">
                                    <wp:extent cx="5272405" cy="1062355"/>
                                    <wp:effectExtent l="0" t="0" r="10795" b="4445"/>
                                    <wp:docPr id="2" name="图片 1"/>
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
                                                    <pic:cNvPr id="2" name="图片 1"/>
                                                    <pic:cNvPicPr>
                                                        <a:picLocks noChangeAspect="1"/>
                                                    </pic:cNvPicPr>
                                                </pic:nvPicPr>
                                                <pic:blipFill>
                                                    <a:blip r:embed="rId7"/>
                                                    <a:stretch>
                                                        <a:fillRect/>
                                                    </a:stretch>
                                                </pic:blipFill>
                                                <pic:spPr>
                                                    <a:xfrm>
                                                        <a:off x="0" y="0"/>
                                                        <a:ext cx="5272405" cy="1062355"/>
                                                    </a:xfrm>
                                                    <a:prstGeom prst="rect">
                                                        <a:avLst/>
                                                    </a:prstGeom>
                                                    <a:noFill/>
                                                    <a:ln w="9525">
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
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="both"/>
                        </w:pPr>
                    </w:p>
                    <w:tbl>
                        <w:tblPr>
                            <w:tblStyle w:val="4"/>
                            <w:tblW w:w="9787" w:type="dxa"/>
                            <w:tblInd w:w="-1086" w:type="dxa"/>
                            <w:tblBorders>
                                <w:top w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:left w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:bottom w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:right w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:insideH w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                <w:insideV w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                            </w:tblBorders>
                            <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                            <w:tblLayout w:type="autofit"/>
                            <w:tblCellMar>
                                <w:top w:w="15" w:type="dxa"/>
                                <w:left w:w="15" w:type="dxa"/>
                                <w:bottom w:w="15" w:type="dxa"/>
                                <w:right w:w="15" w:type="dxa"/>
                            </w:tblCellMar>
                        </w:tblPr>
                        <w:tblGrid>
                            <w:gridCol w:w="667"/>
                            <w:gridCol w:w="733"/>
                            <w:gridCol w:w="934"/>
                            <w:gridCol w:w="906"/>
                            <w:gridCol w:w="867"/>
                            <w:gridCol w:w="800"/>
                            <w:gridCol w:w="747"/>
                            <w:gridCol w:w="693"/>
                            <w:gridCol w:w="627"/>
                            <w:gridCol w:w="480"/>
                            <w:gridCol w:w="440"/>
                            <w:gridCol w:w="546"/>
                            <w:gridCol w:w="694"/>
                            <w:gridCol w:w="653"/>
                        </w:tblGrid>
                        <w:tr>
                            <w:tblPrEx>
                                <w:tblBorders>
                                    <w:top w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:left w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:bottom w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:right w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:insideH w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:insideV w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                </w:tblBorders>
                                <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                <w:tblCellMar>
                                    <w:top w:w="15" w:type="dxa"/>
                                    <w:left w:w="15" w:type="dxa"/>
                                    <w:bottom w:w="15" w:type="dxa"/>
                                    <w:right w:w="15" w:type="dxa"/>
                                </w:tblCellMar>
                            </w:tblPrEx>
                            <w:trPr>
                                <w:trHeight w:val="283" w:hRule="atLeast"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1400" w:type="dxa"/>
                                    <w:gridSpan w:val="2"/>
                                    <w:vMerge w:val="restart"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"
                                                      w:eastAsiaTheme="minorEastAsia"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:eastAsia="zh-Hans"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-Hans" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>数据</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="8387" w:type="dxa"/>
                                    <w:gridSpan w:val="12"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>有功功率（kW）</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        <w:tr>
                            <w:tblPrEx>
                                <w:tblBorders>
                                    <w:top w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:left w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:bottom w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:right w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:insideH w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:insideV w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                </w:tblBorders>
                                <w:tblCellMar>
                                    <w:top w:w="15" w:type="dxa"/>
                                    <w:left w:w="15" w:type="dxa"/>
                                    <w:bottom w:w="15" w:type="dxa"/>
                                    <w:right w:w="15" w:type="dxa"/>
                                </w:tblCellMar>
                            </w:tblPrEx>
                            <w:trPr>
                                <w:trHeight w:val="283" w:hRule="atLeast"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1400" w:type="dxa"/>
                                    <w:gridSpan w:val="2"/>
                                    <w:vMerge w:val="continue"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="934" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>0:00(12)</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="906" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>1:00</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="867" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>2:00</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="800" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>3:00</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="747" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>4:00</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="693" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>5:00</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="627" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>6:00</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="480" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>7:00</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="440" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>8:00</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="546" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>9:00</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="694" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>10:00</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="653" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>11:00</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>

                    <#-- 有功功率表格 start -->
                        <#list table2 as powerList>
                        <w:tr>
                            <w:tblPrEx>
                                <w:tblBorders>
                                    <w:top w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:left w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:bottom w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:right w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:insideH w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:insideV w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                </w:tblBorders>
                                <w:tblCellMar>
                                    <w:top w:w="15" w:type="dxa"/>
                                    <w:left w:w="15" w:type="dxa"/>
                                    <w:bottom w:w="15" w:type="dxa"/>
                                    <w:right w:w="15" w:type="dxa"/>
                                </w:tblCellMar>
                            </w:tblPrEx>
                            <w:trPr>
                                <w:trHeight w:val="283" w:hRule="atLeast"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="667" w:type="dxa"/>
                                    <w:vMerge w:val="restart"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>总</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="733" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>AM</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="934" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="FF0000"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="FF0000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWD0}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="906" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWD1}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="867" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWD2}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="800" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWD3}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="747" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWD4}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="693" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWD5}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="627" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWD6}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="480" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWD7}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="440" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWD8}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="546" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWD9}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="694" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWD10}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="653" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWD11}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        <w:tr>
                            <w:tblPrEx>
                                <w:tblBorders>
                                    <w:top w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:left w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:bottom w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:right w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:insideH w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:insideV w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                </w:tblBorders>
                                <w:tblCellMar>
                                    <w:top w:w="15" w:type="dxa"/>
                                    <w:left w:w="15" w:type="dxa"/>
                                    <w:bottom w:w="15" w:type="dxa"/>
                                    <w:right w:w="15" w:type="dxa"/>
                                </w:tblCellMar>
                            </w:tblPrEx>
                            <w:trPr>
                                <w:trHeight w:val="283" w:hRule="atLeast"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="667" w:type="dxa"/>
                                    <w:vMerge w:val="continue"/>
                                    <w:tcBorders>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="733" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>PM</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="934" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWD12}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="906" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWD13}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="867" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWD14}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="800" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWD15}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="747" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWD16}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="693" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWD17}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="627" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWD18}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="480" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWD19}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="440" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWD20}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="546" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWD21}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="694" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWD22}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="653" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWD23}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        <w:tr>
                            <w:tblPrEx>
                                <w:tblBorders>
                                    <w:top w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:left w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:bottom w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:right w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:insideH w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:insideV w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                </w:tblBorders>
                                <w:tblCellMar>
                                    <w:top w:w="15" w:type="dxa"/>
                                    <w:left w:w="15" w:type="dxa"/>
                                    <w:bottom w:w="15" w:type="dxa"/>
                                    <w:right w:w="15" w:type="dxa"/>
                                </w:tblCellMar>
                            </w:tblPrEx>
                            <w:trPr>
                                <w:trHeight w:val="283" w:hRule="atLeast"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="667" w:type="dxa"/>
                                    <w:vMerge w:val="restart"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>A相</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="733" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>AM</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="934" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="FF0000"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="FF0000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWA0}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="906" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWA1}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="867" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWA2}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="800" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWA3}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="747" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWA4}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="693" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWA5}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="627" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWA6}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="480" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWA7}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="440" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWA8}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="546" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWA9}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="694" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWA10}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="653" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWA11}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        <w:tr>
                            <w:tblPrEx>
                                <w:tblBorders>
                                    <w:top w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:left w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:bottom w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:right w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:insideH w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:insideV w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                </w:tblBorders>
                                <w:tblCellMar>
                                    <w:top w:w="15" w:type="dxa"/>
                                    <w:left w:w="15" w:type="dxa"/>
                                    <w:bottom w:w="15" w:type="dxa"/>
                                    <w:right w:w="15" w:type="dxa"/>
                                </w:tblCellMar>
                            </w:tblPrEx>
                            <w:trPr>
                                <w:trHeight w:val="283" w:hRule="atLeast"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="667" w:type="dxa"/>
                                    <w:vMerge w:val="continue"/>
                                    <w:tcBorders>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="733" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>PM</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="934" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWA12}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="906" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWA13}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="867" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWA14}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="800" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWA15}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="747" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWA16}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="693" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWA17}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="627" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWA18}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="480" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWA19}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="440" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWA20}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="546" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWA21}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="694" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWA22}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="653" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWA23}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        <w:tr>
                            <w:tblPrEx>
                                <w:tblBorders>
                                    <w:top w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:left w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:bottom w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:right w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:insideH w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:insideV w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                </w:tblBorders>
                                <w:tblCellMar>
                                    <w:top w:w="15" w:type="dxa"/>
                                    <w:left w:w="15" w:type="dxa"/>
                                    <w:bottom w:w="15" w:type="dxa"/>
                                    <w:right w:w="15" w:type="dxa"/>
                                </w:tblCellMar>
                            </w:tblPrEx>
                            <w:trPr>
                                <w:trHeight w:val="283" w:hRule="atLeast"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="667" w:type="dxa"/>
                                    <w:vMerge w:val="restart"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>B相</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="733" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>AM</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="934" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWB0}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="906" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWB1}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="867" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWB2}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="800" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWB3}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="747" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWB4}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="693" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWB5}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="627" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWB6}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="480" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWB7}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="440" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWB8}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="546" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWB9}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="694" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWB10}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="653" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWB11}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        <w:tr>
                            <w:tblPrEx>
                                <w:tblBorders>
                                    <w:top w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:left w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:bottom w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:right w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:insideH w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:insideV w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                </w:tblBorders>
                                <w:tblCellMar>
                                    <w:top w:w="15" w:type="dxa"/>
                                    <w:left w:w="15" w:type="dxa"/>
                                    <w:bottom w:w="15" w:type="dxa"/>
                                    <w:right w:w="15" w:type="dxa"/>
                                </w:tblCellMar>
                            </w:tblPrEx>
                            <w:trPr>
                                <w:trHeight w:val="283" w:hRule="atLeast"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="667" w:type="dxa"/>
                                    <w:vMerge w:val="continue"/>
                                    <w:tcBorders>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="733" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>PM</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="934" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWB12}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="906" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWB13}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="867" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWB14}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="800" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWB15}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="747" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWB16}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="693" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWB17}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="627" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWB18}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="480" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWB19}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="440" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWB20}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="546" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWB21}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="694" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWB22}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="653" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWB23}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        <w:tr>
                            <w:tblPrEx>
                                <w:tblBorders>
                                    <w:top w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:left w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:bottom w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:right w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:insideH w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:insideV w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                </w:tblBorders>
                                <w:tblCellMar>
                                    <w:top w:w="15" w:type="dxa"/>
                                    <w:left w:w="15" w:type="dxa"/>
                                    <w:bottom w:w="15" w:type="dxa"/>
                                    <w:right w:w="15" w:type="dxa"/>
                                </w:tblCellMar>
                            </w:tblPrEx>
                            <w:trPr>
                                <w:trHeight w:val="283" w:hRule="atLeast"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="667" w:type="dxa"/>
                                    <w:vMerge w:val="restart"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>C相</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="733" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>AM</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="934" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="FF0000"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="FF0000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWC0}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="906" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWC1}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="867" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWC2}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="800" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWC3}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="747" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWC4}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="693" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWC5}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="627" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWC6}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="480" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWC7}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="440" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWC8}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="546" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWC9}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="694" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWC10}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="653" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWC11}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        <w:tr>
                            <w:tblPrEx>
                                <w:tblBorders>
                                    <w:top w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:left w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:bottom w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:right w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:insideH w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                    <w:insideV w:val="none" w:color="auto" w:sz="0" w:space="0"/>
                                </w:tblBorders>
                                <w:tblCellMar>
                                    <w:top w:w="15" w:type="dxa"/>
                                    <w:left w:w="15" w:type="dxa"/>
                                    <w:bottom w:w="15" w:type="dxa"/>
                                    <w:right w:w="15" w:type="dxa"/>
                                </w:tblCellMar>
                            </w:tblPrEx>
                            <w:trPr>
                                <w:trHeight w:val="283" w:hRule="atLeast"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="667" w:type="dxa"/>
                                    <w:vMerge w:val="continue"/>
                                    <w:tcBorders>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="733" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>PM</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="934" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWC12}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="906" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWC13}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="867" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWC14}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="800" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWC15}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="747" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWC16}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="693" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWC17}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="627" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWC18}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="480" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWC19}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="440" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWC20}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="546" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWC21}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="694" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWC22}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="653" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:left w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:bottom w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                        <w:right w:val="single" w:color="000000" w:sz="4" w:space="0"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:keepNext w:val="0"/>
                                        <w:keepLines w:val="0"/>
                                        <w:widowControl/>
                                        <w:suppressLineNumbers w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:textAlignment w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Arial" w:hAnsi="Arial" w:eastAsia="宋体"
                                                      w:cs="Arial"/>
                                            <w:i w:val="0"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="20"/>
                                            <w:szCs w:val="20"/>
                                            <w:u w:val="none"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar"/>
                                        </w:rPr>
                                        <w:t>${powerList.kWC23}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        </#list>
                    <#-- 有功功率表格end -->
                    </w:tbl>


                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="default" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                                <w:lang w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t xml:space="preserve">1.2.2 </w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>谐波情况</w:t>
                        </w:r>
                    </w:p>

                <#-- 谐波情况 start -->
                     <#list table3 as harPlan>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="1"/>
                            </w:numPr>
                            <w:ind w:left="420" w:leftChars="0" w:hanging="420" w:firstLineChars="0"/>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Bold" w:hAnsi="Songti SC Bold"
                                          w:eastAsia="Songti SC Bold" w:cs="Songti SC Bold"/>
                                <w:b/>
                                <w:bCs w:val="0"/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Bold" w:hAnsi="Songti SC Bold"
                                          w:eastAsia="Songti SC Bold" w:cs="Songti SC Bold"/>
                                <w:b/>
                                <w:bCs w:val="0"/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>${harPlan.harName}</w:t>
                        </w:r>
                    </w:p>
                         <#list harPlan.harText as harIText>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:ind w:firstLine="420" w:firstLineChars="0"/>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="32"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="32"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>${harIText}</w:t> <#-- 谐波电流 -->
                        </w:r>
                    </w:p>
                         </#list>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="both"/>
                        </w:pPr>
                        <w:r>
                            <w:drawing>
                                <wp:inline distT="0" distB="0" distL="114300" distR="114300">
                                    <wp:extent cx="5258435" cy="1071245"/>
                                    <wp:effectExtent l="0" t="0" r="24765" b="20955"/>
                                    <wp:docPr id="3" name="图片 2"/>
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
                                                    <pic:cNvPr id="3" name="图片 2"/>
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
                                                        <a:ext cx="5258435" cy="1071245"/>
                                                    </a:xfrm>
                                                    <a:prstGeom prst="rect">
                                                        <a:avLst/>
                                                    </a:prstGeom>
                                                    <a:noFill/>
                                                    <a:ln w="9525">
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
                            <w:tblStyle w:val="5"/>
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
                            <w:gridCol w:w="877"/>
                            <w:gridCol w:w="880"/>
                            <w:gridCol w:w="1906"/>
                            <w:gridCol w:w="854"/>
                            <w:gridCol w:w="2026"/>
                            <w:gridCol w:w="960"/>
                            <w:gridCol w:w="1995"/>
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
                                    <w:tcW w:w="877" w:type="dxa"/>
                                    <w:vMerge w:val="restart"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>谐波</w:t>
                                    </w:r>
                                </w:p>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>次数</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2786" w:type="dxa"/>
                                    <w:gridSpan w:val="2"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>A相谐波电流</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2880" w:type="dxa"/>
                                    <w:gridSpan w:val="2"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>B相谐波电流</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2955" w:type="dxa"/>
                                    <w:gridSpan w:val="2"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
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
                                    <w:tcW w:w="877" w:type="dxa"/>
                                    <w:vMerge w:val="continue"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="880" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>日最大值</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>(A)</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1906" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>发生时间</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="854" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>日最大值</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>(A)</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2026" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>发生时间</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="960" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>日最大值</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>(A)</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1995" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
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
                                    <w:tcW w:w="877" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                        <w:t>${harList.num}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="880" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                        <w:t>${harList.a_max}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1906" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                        <w:t>${harList.a_time}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="854" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                        <w:t>${harList.b_max}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2026" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                        <w:t>${harList.b_time}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="960" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                        <w:t>${harList.c_max}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1995" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                        <w:t>${harList.c_time}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                         </#list>
                    </w:tbl>
                     </#list>
                <#-- 谐波情况 end -->
                    <#list table4 as harVPlan>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="both"/>
                        </w:pPr>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="1"/>
                            </w:numPr>
                            <w:ind w:left="420" w:leftChars="0" w:hanging="420" w:firstLineChars="0"/>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Bold" w:hAnsi="Songti SC Bold"
                                          w:eastAsia="Songti SC Bold" w:cs="Songti SC Bold"/>
                                <w:b/>
                                <w:bCs w:val="0"/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Bold" w:hAnsi="Songti SC Bold"
                                          w:eastAsia="Songti SC Bold" w:cs="Songti SC Bold"/>
                                <w:b/>
                                <w:bCs w:val="0"/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>${harVPlan.harName}</w:t>
                        </w:r>
                    </w:p>

                        <#list harVPlan.harText as harVText>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:ind w:firstLine="420" w:firstLineChars="0"/>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>${harVText}</w:t><#-- 谐波电压 -->
                        </w:r>
                    </w:p>
                        </#list>

                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="left"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:drawing>
                                <wp:inline distT="0" distB="0" distL="114300" distR="114300">
                                    <wp:extent cx="5272405" cy="1078230"/>
                                    <wp:effectExtent l="0" t="0" r="10795" b="13970"/>
                                    <wp:docPr id="5" name="图片 3"/>
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
                                                    <pic:cNvPr id="5" name="图片 3"/>
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
                                                        <a:ext cx="5272405" cy="1078230"/>
                                                    </a:xfrm>
                                                    <a:prstGeom prst="rect">
                                                        <a:avLst/>
                                                    </a:prstGeom>
                                                    <a:noFill/>
                                                    <a:ln w="9525">
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
                            <w:tblStyle w:val="5"/>
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
                            <w:gridCol w:w="903"/>
                            <w:gridCol w:w="880"/>
                            <w:gridCol w:w="1880"/>
                            <w:gridCol w:w="867"/>
                            <w:gridCol w:w="2027"/>
                            <w:gridCol w:w="933"/>
                            <w:gridCol w:w="2008"/>
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
                                    <w:tcW w:w="903" w:type="dxa"/>
                                    <w:vMerge w:val="restart"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>谐波</w:t>
                                    </w:r>
                                </w:p>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>次数</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2760" w:type="dxa"/>
                                    <w:gridSpan w:val="2"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>A相谐波电</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                                        </w:rPr>
                                        <w:t>压</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2894" w:type="dxa"/>
                                    <w:gridSpan w:val="2"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>B相谐波电</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                                        </w:rPr>
                                        <w:t>流</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2941" w:type="dxa"/>
                                    <w:gridSpan w:val="2"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>C相谐波电</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                                        </w:rPr>
                                        <w:t>流</w:t>
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
                                    <w:tcW w:w="903" w:type="dxa"/>
                                    <w:vMerge w:val="continue"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="880" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>日最大值</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>(</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>%</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>)</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1880" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>发生时间</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="867" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>日最大值</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>(</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>%</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>)</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2027" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>发生时间</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="933" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>日最大值</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>(</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="default" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>%</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>)</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2008" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
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
                                    <w:tcW w:w="903" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="FFFFFF" w:themeFill="background1"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                        <w:t>${harList.num}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="880" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="FFFFFF" w:themeFill="background1"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                        <w:t>${harList.a_max}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1880" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="FFFFFF" w:themeFill="background1"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                        <w:t>${harList.a_time}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="867" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="FFFFFF" w:themeFill="background1"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                        <w:t>${harList.b_max}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2027" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="FFFFFF" w:themeFill="background1"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                        <w:t>${harList.b_time}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="933" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="FFFFFF" w:themeFill="background1"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                        <w:t>${harList.c_max}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2008" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="FFFFFF" w:themeFill="background1"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
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
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                                <w:highlight w:val="none"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="default" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                                <w:highlight w:val="none"/>
                                <w:lang w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t xml:space="preserve">1.2.3 </w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                                <w:highlight w:val="none"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>不平衡度情况</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:ind w:firstLine="420" w:firstLineChars="0"/>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:highlight w:val="none"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:highlight w:val="none"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>企业设备的电压、电流日不平衡情况评价：</w:t>
                        </w:r>
                    </w:p>

                    <w:tbl>
                        <w:tblPr>
                            <w:tblStyle w:val="5"/>
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
                            <w:gridCol w:w="1091"/>
                            <w:gridCol w:w="1846"/>
                            <w:gridCol w:w="1827"/>
                            <w:gridCol w:w="2400"/>
                            <w:gridCol w:w="2331"/>
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
                                    <w:tcW w:w="1091" w:type="dxa"/>
                                    <w:vMerge w:val="restart"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:spacing w:line="240" w:lineRule="auto"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>设备</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3673" w:type="dxa"/>
                                    <w:gridSpan w:val="2"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:spacing w:line="240" w:lineRule="auto"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>电压不平衡度</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="4731" w:type="dxa"/>
                                    <w:gridSpan w:val="2"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:spacing w:line="240" w:lineRule="auto"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>电</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                                        </w:rPr>
                                        <w:t>流</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>不平衡度</w:t>
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
                                    <w:tcW w:w="1091" w:type="dxa"/>
                                    <w:vMerge w:val="continue"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:spacing w:line="240" w:lineRule="auto"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1846" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:spacing w:line="240" w:lineRule="auto"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>不平衡度</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1827" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:spacing w:line="240" w:lineRule="auto"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                                        </w:rPr>
                                        <w:t>评价</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2400" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:spacing w:line="240" w:lineRule="auto"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>不平衡度</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2331" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:spacing w:line="240" w:lineRule="auto"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                                        </w:rPr>
                                        <w:t>评价</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                    <#-- 不平衡度汇总 -->
                        <#list canUseParam as canUse>
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
                                    <w:tcW w:w="1091" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:spacing w:line="240" w:lineRule="auto"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                        <w:t>${canUse.canUseName}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1846" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:spacing w:line="240" w:lineRule="auto"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                        <w:t>${canUse.unbalancedVData}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1827" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:spacing w:line="240" w:lineRule="auto"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-Hans" w:bidi="ar-SA"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                        <w:t>${canUse.msgVSecondP}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2400" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="top"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:spacing w:line="240" w:lineRule="auto"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:kern w:val="2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-Hans" w:bidi="ar-SA"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                        <w:t>${canUse.unbalancedIData}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2331" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:spacing w:line="240" w:lineRule="auto"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                        <w:t>${canUse.msgISecondP}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        </#list>
                    </w:tbl>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="32"/>
                                <w:szCs w:val="32"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="default" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="32"/>
                                <w:szCs w:val="32"/>
                                <w:lang w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t xml:space="preserve">1.3 </w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="32"/>
                                <w:szCs w:val="32"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>能效分析</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="1"/>
                            </w:numPr>
                            <w:ind w:left="420" w:leftChars="0" w:hanging="420" w:firstLineChars="0"/>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Bold" w:hAnsi="Songti SC Bold"
                                          w:eastAsia="Songti SC Bold" w:cs="Songti SC Bold"/>
                                <w:b/>
                                <w:bCs w:val="0"/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Bold" w:hAnsi="Songti SC Bold"
                                          w:eastAsia="Songti SC Bold" w:cs="Songti SC Bold"/>
                                <w:b/>
                                <w:bCs w:val="0"/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>行业对标</w:t>
                        </w:r>
                    </w:p>
                    <w:tbl>
                        <w:tblPr>
                            <w:tblStyle w:val="5"/>
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
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
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
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>吨电耗</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
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
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>吨电费</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
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
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                        <w:t>行业平均</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3044" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                        <w:t>${industryEData.AVGPW}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3366" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
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
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                        <w:t>行业最优</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3044" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                        <w:t>${industryEData.MINPW}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3366" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
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
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                        <w:t>行业最差</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3044" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                        <w:t>${industryEData.MAXPW}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3366" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
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
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                        <w:t>设备平均</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3044" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                        <w:t>${industryEData.MAVGPW}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3366" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
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
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                        <w:t>设备最优</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3044" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                        <w:t>${industryEData.MMINPW}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3366" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
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
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                        <w:t>设备最差</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3044" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                        <w:t>${industryEData.MMAXPW}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3366" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                        <w:t>${industryEData.MMAXFEE}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                    </w:tbl>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="1"/>
                            </w:numPr>
                            <w:ind w:left="420" w:leftChars="0" w:hanging="420" w:firstLineChars="0"/>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:asciiTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia"
                                          w:eastAsiaTheme="minorEastAsia" w:cstheme="minorEastAsia"/>
                                <w:i w:val="0"/>
                                <w:caps w:val="0"/>
                                <w:color w:val="4B4B4B"/>
                                <w:spacing w:val="0"/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:shd w:val="clear" w:fill="FFFFFF"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Bold" w:hAnsi="Songti SC Bold"
                                          w:eastAsia="Songti SC Bold" w:cs="Songti SC Bold"/>
                                <w:b/>
                                <w:bCs w:val="0"/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>企业能效</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:pStyle w:val="3"/>
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
                                <w:rFonts w:hint="eastAsia" w:asciiTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia"
                                          w:eastAsiaTheme="minorEastAsia" w:cstheme="minorEastAsia"/>
                                <w:i w:val="0"/>
                                <w:caps w:val="0"/>
                                <w:color w:val="4B4B4B"/>
                                <w:spacing w:val="0"/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:asciiTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia"
                                          w:cstheme="minorEastAsia"/>
                                <w:i w:val="0"/>
                                <w:caps w:val="0"/>
                                <w:color w:val="4B4B4B"/>
                                <w:spacing w:val="0"/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:shd w:val="clear" w:fill="FFFFFF"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>企业</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:asciiTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia"
                                          w:eastAsiaTheme="minorEastAsia" w:cstheme="minorEastAsia"/>
                                <w:i w:val="0"/>
                                <w:caps w:val="0"/>
                                <w:color w:val="4B4B4B"/>
                                <w:spacing w:val="0"/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:shd w:val="clear" w:fill="FFFFFF"/>
                            </w:rPr>
                            <w:t>${industryEData.pwText}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:asciiTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia"
                                          w:eastAsiaTheme="minorEastAsia" w:cstheme="minorEastAsia"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:asciiTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia"
                                          w:cstheme="minorEastAsia"/>
                                <w:i w:val="0"/>
                                <w:caps w:val="0"/>
                                <w:color w:val="4B4B4B"/>
                                <w:spacing w:val="0"/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:shd w:val="clear" w:fill="FFFFFF"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>企业</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:asciiTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia"
                                          w:eastAsiaTheme="minorEastAsia" w:cstheme="minorEastAsia"/>
                                <w:i w:val="0"/>
                                <w:caps w:val="0"/>
                                <w:color w:val="4B4B4B"/>
                                <w:spacing w:val="0"/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:shd w:val="clear" w:fill="FFFFFF"/>
                            </w:rPr>
                            <w:t>${industryEData.feeText}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="1"/>
                            </w:numPr>
                            <w:ind w:left="420" w:leftChars="0" w:hanging="420" w:firstLineChars="0"/>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Bold" w:hAnsi="Songti SC Bold"
                                          w:eastAsia="Songti SC Bold" w:cs="Songti SC Bold"/>
                                <w:b/>
                                <w:bCs w:val="0"/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Bold" w:hAnsi="Songti SC Bold"
                                          w:eastAsia="Songti SC Bold" w:cs="Songti SC Bold"/>
                                <w:b/>
                                <w:bCs w:val="0"/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>企业设备能效</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:pStyle w:val="3"/>
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
                                <w:rFonts w:hint="eastAsia" w:asciiTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia"
                                          w:eastAsiaTheme="minorEastAsia" w:cstheme="minorEastAsia"/>
                                <w:i w:val="0"/>
                                <w:caps w:val="0"/>
                                <w:color w:val="4B4B4B"/>
                                <w:spacing w:val="0"/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:shd w:val="clear" w:fill="FFFFFF"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="default" w:asciiTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia"
                                          w:cstheme="minorEastAsia"/>
                                <w:i w:val="0"/>
                                <w:caps w:val="0"/>
                                <w:color w:val="4B4B4B"/>
                                <w:spacing w:val="0"/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:shd w:val="clear" w:fill="FFFFFF"/>
                                <w:lang w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>${industryEData.meterMINName}-生产能效最优为：${industryEData.meterMINPW} 千瓦时/吨</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:pStyle w:val="3"/>
                            <w:keepNext w:val="0"/>
                            <w:keepLines w:val="0"/>
                            <w:widowControl/>
                            <w:suppressLineNumbers w:val="0"/>-
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
                                <w:rFonts w:hint="eastAsia" w:asciiTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia"
                                          w:cstheme="minorEastAsia"/>
                                <w:i w:val="0"/>
                                <w:caps w:val="0"/>
                                <w:color w:val="4B4B4B"/>
                                <w:spacing w:val="0"/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:shd w:val="clear" w:fill="FFFFFF"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="default" w:asciiTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia"
                                          w:cstheme="minorEastAsia"/>
                                <w:i w:val="0"/>
                                <w:caps w:val="0"/>
                                <w:color w:val="4B4B4B"/>
                                <w:spacing w:val="0"/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:shd w:val="clear" w:fill="FFFFFF"/>
                                <w:lang w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>${industryEData.meterMAXName}-生产能效最差为：${industryEData.meterMAXPW} 千瓦时/吨</w:t>
                        </w:r>
                    </w:p>
                <#-- 企业设备能效图片 -->
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:ind w:leftChars="0"/>
                            <w:jc w:val="left"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:drawing>
                                <wp:inline distT="0" distB="0" distL="114300" distR="114300">
                                    <wp:extent cx="5269230" cy="1092200"/>
                                    <wp:effectExtent l="0" t="0" r="13970" b="0"/>
                                    <wp:docPr id="13" name="图片 3"/>
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
                                                    <pic:cNvPr id="13" name="图片 3"/>
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
                            <w:tblStyle w:val="5"/>
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
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:spacing w:line="240" w:lineRule="auto"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
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
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:spacing w:line="240" w:lineRule="auto"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>吨电耗</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
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
                                    <w:shd w:val="clear" w:color="auto" w:fill="5B9BD5" w:themeFill="accent1"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:spacing w:line="240" w:lineRule="auto"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>吨电费</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="F2F2F2" w:themeColor="background1" w:themeShade="F2"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
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
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:spacing w:line="240" w:lineRule="auto"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                        <w:t>${eList.METER_NAME}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3044" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:spacing w:line="240" w:lineRule="auto"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                        <w:t>${eList.DATA}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3366" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                </w:tcPr>
                                <w:p>
                                    <w:pPr>
                                        <w:spacing w:line="240" w:lineRule="auto"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular"
                                                      w:hAnsi="Songti SC Regular" w:eastAsia="Songti SC Regular"
                                                      w:cs="Songti SC Regular"/>
                                            <w:color w:val="000000" w:themeColor="text1"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:vertAlign w:val="baseline"/>
                                            <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                                            <w14:textFill>
                                                <w14:solidFill>
                                                    <w14:schemeClr w14:val="tx1"/>
                                                </w14:solidFill>
                                            </w14:textFill>
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
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="36"/>
                                <w:szCs w:val="36"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="default" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="36"/>
                                <w:szCs w:val="36"/>
                                <w:lang w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t xml:space="preserve">2. </w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="36"/>
                                <w:szCs w:val="36"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>设备用能分析</w:t>
                        </w:r>
                    </w:p>
                <#-- 设备用能分析 start -->
                    <#list canUseParam as canUse>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="default" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                                <w:lang w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t xml:space="preserve">2.${canUse.i} </w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t> ${canUse.canUseName}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="default" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                                <w:lang w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t xml:space="preserve">2.${canUse.i}.1 </w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>负载率分析</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:ind w:firstLine="420" w:firstLineChars="0"/>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>${canUse.loadFirstP}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:ind w:firstLine="420" w:firstLineChars="0"/>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>${canUse.loadSecondP}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:ind w:firstLine="420" w:firstLineChars="0"/>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>${canUse.loadThirdP}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="both"/>
                        </w:pPr>
                        <w:r>
                            <w:drawing>
                                <wp:inline distT="0" distB="0" distL="114300" distR="114300">
                                    <wp:extent cx="5258435" cy="828675"/>
                                    <wp:effectExtent l="0" t="0" r="24765" b="9525"/>
                                    <wp:docPr id="6" name="图片 4"/>
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
                                                    <pic:cNvPr id="6" name="图片 4"/>
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
                                                        <a:ext cx="5258435" cy="828675"/>
                                                    </a:xfrm>
                                                    <a:prstGeom prst="rect">
                                                        <a:avLst/>
                                                    </a:prstGeom>
                                                    <a:noFill/>
                                                    <a:ln w="9525">
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
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="center"/>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b w:val="0"/>
                                <w:bCs w:val="0"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>负载率曲线</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="both"/>
                        </w:pPr>
                        <w:r>
                            <w:drawing>
                                <wp:inline distT="0" distB="0" distL="114300" distR="114300">
                                    <wp:extent cx="5261610" cy="815975"/>
                                    <wp:effectExtent l="0" t="0" r="21590" b="22225"/>
                                    <wp:docPr id="8" name="图片 5"/>
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
                                                    <pic:cNvPr id="8" name="图片 5"/>
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
                                                        <a:ext cx="5261610" cy="815975"/>
                                                    </a:xfrm>
                                                    <a:prstGeom prst="rect">
                                                        <a:avLst/>
                                                    </a:prstGeom>
                                                    <a:noFill/>
                                                    <a:ln w="9525">
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
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b w:val="0"/>
                                <w:bCs w:val="0"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>负载率发生时间曲线</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="default" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                                <w:lang w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t xml:space="preserve">2.${canUse.i}.2 </w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Light" w:hAnsi="Heiti SC Light"
                                          w:eastAsia="Heiti SC Light" w:cs="Heiti SC Light"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>日不平衡度分析</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="2"/>
                            </w:numPr>
                            <w:ind w:left="420" w:leftChars="0" w:hanging="420" w:firstLineChars="0"/>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Medium" w:hAnsi="Heiti SC Medium"
                                          w:eastAsia="Heiti SC Medium" w:cs="Heiti SC Medium"/>
                                <w:b/>
                                <w:bCs w:val="0"/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Medium" w:hAnsi="Heiti SC Medium"
                                          w:eastAsia="Heiti SC Medium" w:cs="Heiti SC Medium"/>
                                <w:b/>
                                <w:bCs w:val="0"/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>电压不平衡度分析</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:ind w:leftChars="0" w:firstLine="420" w:firstLineChars="0"/>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>${canUse.msgVFirstP}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:ind w:leftChars="0" w:firstLine="420" w:firstLineChars="0"/>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>${canUse.msgVSecondP}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:ind w:firstLine="420" w:firstLineChars="0"/>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="default" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>${canUse.msgVThirdP}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="both"/>
                        </w:pPr>
                        <w:r>
                            <w:drawing>
                                <wp:inline distT="0" distB="0" distL="114300" distR="114300">
                                    <wp:extent cx="5261610" cy="800100"/>
                                    <wp:effectExtent l="0" t="0" r="21590" b="12700"/>
                                    <wp:docPr id="12" name="图片 9"/>
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
                                                    <pic:cNvPr id="12" name="图片 9"/>
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
                                                        <a:ext cx="5261610" cy="800100"/>
                                                    </a:xfrm>
                                                    <a:prstGeom prst="rect">
                                                        <a:avLst/>
                                                    </a:prstGeom>
                                                    <a:noFill/>
                                                    <a:ln w="9525">
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
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:hint="default"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="default"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>不平衡度最大值</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:drawing>
                                <wp:inline distT="0" distB="0" distL="114300" distR="114300">
                                    <wp:extent cx="5267325" cy="1654810"/>
                                    <wp:effectExtent l="0" t="0" r="15875" b="21590"/>
                                    <wp:docPr id="14" name="图片 10"/>
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
                                                    <pic:cNvPr id="14" name="图片 10"/>
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
                                                        <a:ext cx="5267325" cy="1654810"/>
                                                    </a:xfrm>
                                                    <a:prstGeom prst="rect">
                                                        <a:avLst/>
                                                    </a:prstGeom>
                                                    <a:noFill/>
                                                    <a:ln w="9525">
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
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:hint="default"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="default"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>不平衡度最大值发生时间</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="both"/>
                        </w:pPr>
                        <w:r>
                            <w:drawing>
                                <wp:inline distT="0" distB="0" distL="114300" distR="114300">
                                    <wp:extent cx="5266055" cy="1635760"/>
                                    <wp:effectExtent l="0" t="0" r="17145" b="15240"/>
                                    <wp:docPr id="16" name="图片 12"/>
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
                                                    <pic:cNvPr id="16" name="图片 12"/>
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
                                                        <a:ext cx="5266055" cy="1635760"/>
                                                    </a:xfrm>
                                                    <a:prstGeom prst="rect">
                                                        <a:avLst/>
                                                    </a:prstGeom>
                                                    <a:noFill/>
                                                    <a:ln w="9525">
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
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="center"/>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="default"/>
                            </w:rPr>
                            <w:t>不平衡度越限日累计时间</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="2"/>
                            </w:numPr>
                            <w:ind w:left="420" w:leftChars="0" w:hanging="420" w:firstLineChars="0"/>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Heiti SC Medium" w:hAnsi="Heiti SC Medium"
                                          w:eastAsia="Heiti SC Medium" w:cs="Heiti SC Medium"/>
                                <w:b/>
                                <w:bCs w:val="0"/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>电流不平衡度分析</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:ind w:leftChars="0" w:firstLine="420" w:firstLineChars="0"/>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>${canUse.msgIFirstP}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:ind w:leftChars="0" w:firstLine="420" w:firstLineChars="0"/>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>${canUse.msgISecondP}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:ind w:leftChars="0" w:firstLine="420" w:firstLineChars="0"/>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="default" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-CN"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia" w:ascii="Songti SC Regular" w:hAnsi="Songti SC Regular"
                                          w:eastAsia="Songti SC Regular" w:cs="Songti SC Regular"/>
                                <w:b w:val="0"/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>${canUse.msgIThirdP}</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="both"/>
                        </w:pPr>
                        <w:r>
                            <w:drawing>
                                <wp:inline distT="0" distB="0" distL="114300" distR="114300">
                                    <wp:extent cx="5261610" cy="809625"/>
                                    <wp:effectExtent l="0" t="0" r="21590" b="3175"/>
                                    <wp:docPr id="9" name="图片 6"/>
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
                                                    <pic:cNvPr id="9" name="图片 6"/>
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
                                                        <a:ext cx="5261610" cy="809625"/>
                                                    </a:xfrm>
                                                    <a:prstGeom prst="rect">
                                                        <a:avLst/>
                                                    </a:prstGeom>
                                                    <a:noFill/>
                                                    <a:ln w="9525">
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
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>不平衡度最大值曲线</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:drawing>
                                <wp:inline distT="0" distB="0" distL="114300" distR="114300">
                                    <wp:extent cx="5266055" cy="1661160"/>
                                    <wp:effectExtent l="0" t="0" r="17145" b="15240"/>
                                    <wp:docPr id="10" name="图片 7"/>
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
                                                    <pic:cNvPr id="10" name="图片 7"/>
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
                                                        <a:ext cx="5266055" cy="1661160"/>
                                                    </a:xfrm>
                                                    <a:prstGeom prst="rect">
                                                        <a:avLst/>
                                                    </a:prstGeom>
                                                    <a:noFill/>
                                                    <a:ln w="9525">
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
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:ind w:leftChars="0" w:firstLine="420" w:firstLineChars="0"/>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>不平衡度最大值发生时间曲线</w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="both"/>
                        </w:pPr>
                        <w:r>
                            <w:drawing>
                                <wp:inline distT="0" distB="0" distL="114300" distR="114300">
                                    <wp:extent cx="5267325" cy="1642110"/>
                                    <wp:effectExtent l="0" t="0" r="15875" b="8890"/>
                                    <wp:docPr id="11" name="图片 8"/>
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
                                                    <pic:cNvPr id="11" name="图片 8"/>
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
                                                        <a:ext cx="5267325" cy="1642110"/>
                                                    </a:xfrm>
                                                    <a:prstGeom prst="rect">
                                                        <a:avLst/>
                                                    </a:prstGeom>
                                                    <a:noFill/>
                                                    <a:ln w="9525">
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
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:ind w:leftChars="0" w:firstLine="420" w:firstLineChars="0"/>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="default"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                            <w:t>不平衡度越限日累计时间</w:t>
                        </w:r>
                    </w:p>
                    </#list>
                <#-- 设备用能分析 end -->
                    <w:p>
                        <w:pPr>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="0"/>
                            </w:numPr>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:lang w:val="en-US" w:eastAsia="zh-Hans"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:sectPr>
                        <w:pgSz w:w="11906" w:h="16838"/>
                        <w:pgMar w:top="1440" w:right="1800" w:bottom="1440" w:left="1800" w:header="851" w:footer="992"
                                 w:gutter="0"/>
                        <w:pgBorders>
                            <w:top w:val="none" w:sz="0" w:space="0"/>
                            <w:left w:val="none" w:sz="0" w:space="0"/>
                            <w:bottom w:val="none" w:sz="0" w:space="0"/>
                            <w:right w:val="none" w:sz="0" w:space="0"/>
                        </w:pgBorders>
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
                <Pages>1</Pages>
                <Words>0</Words>
                <Characters>0</Characters>
                <Lines>0</Lines>
                <Paragraphs>0</Paragraphs>
                <TotalTime>187</TotalTime>
                <ScaleCrop>false</ScaleCrop>
                <LinksUpToDate>false</LinksUpToDate>
                <CharactersWithSpaces>0</CharactersWithSpaces>
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
                <dcterms:created xsi:type="dcterms:W3CDTF">2020-12-28T10:06:00Z</dcterms:created>
                <dc:creator>chenl</dc:creator>
                <cp:lastModifiedBy>Administrator</cp:lastModifiedBy>
                <dcterms:modified xsi:type="dcterms:W3CDTF">2020-12-28T09:35:07Z</dcterms:modified>
                <cp:revision>1</cp:revision>
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
                <w:font w:name="Heiti SC Light">
                    <w:altName w:val="宋体"/>
                    <w:panose1 w:val="02000000000000000000"/>
                    <w:charset w:val="86"/>
                    <w:family w:val="auto"/>
                    <w:pitch w:val="default"/>
                    <w:sig w:usb0="00000000" w:usb1="00000000" w:usb2="00000000" w:usb3="00000000" w:csb0="203E0000"
                           w:csb1="00000000"/>
                </w:font>
                <w:font w:name="Songti SC Regular">
                    <w:altName w:val="宋体"/>
                    <w:panose1 w:val="02010800040101010101"/>
                    <w:charset w:val="86"/>
                    <w:family w:val="auto"/>
                    <w:pitch w:val="default"/>
                    <w:sig w:usb0="00000000" w:usb1="00000000" w:usb2="00000000" w:usb3="00000000" w:csb0="00040000"
                           w:csb1="00000000"/>
                </w:font>
                <w:font w:name="Songti SC Bold">
                    <w:altName w:val="宋体"/>
                    <w:panose1 w:val="02010800040101010101"/>
                    <w:charset w:val="86"/>
                    <w:family w:val="auto"/>
                    <w:pitch w:val="default"/>
                    <w:sig w:usb0="00000000" w:usb1="00000000" w:usb2="00000000" w:usb3="00000000" w:csb0="00040000"
                           w:csb1="00000000"/>
                </w:font>
                <w:font w:name="Heiti SC Medium">
                    <w:altName w:val="宋体"/>
                    <w:panose1 w:val="02000000000000000000"/>
                    <w:charset w:val="86"/>
                    <w:family w:val="auto"/>
                    <w:pitch w:val="default"/>
                    <w:sig w:usb0="00000000" w:usb1="00000000" w:usb2="00000000" w:usb3="00000000" w:csb0="203E0000"
                           w:csb1="00000000"/>
                </w:font>
            </w:fonts>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/word/media/image1.png" pkg:contentType="image/png">
        <pkg:binaryData>${chart1}</pkg:binaryData>
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
<#--<pkg:part pkg:name="/word/media/image10.png" pkg:contentType="image/png">-->
<#--<pkg:binaryData>-->
<#--</pkg:binaryData>-->
<#--</pkg:part>-->
<#--<pkg:part pkg:name="/word/media/image11.png" pkg:contentType="image/png">-->
<#--<pkg:binaryData>-->
<#--</pkg:binaryData>-->
<#--</pkg:part>-->
<#--<pkg:part pkg:name="/word/media/image12.png" pkg:contentType="image/png">-->
<#--<pkg:binaryData>-->
<#--</pkg:binaryData>-->
<#--</pkg:part>-->
<#--<pkg:part pkg:name="/word/media/image13.png" pkg:contentType="image/png">-->
<#--<pkg:binaryData>-->
<#--</pkg:binaryData>-->
<#--</pkg:part>-->
<#--<pkg:part pkg:name="/word/media/image14.png" pkg:contentType="image/png">-->
<#--<pkg:binaryData>-->
<#--</pkg:binaryData>-->
<#--</pkg:part>-->
<#--<pkg:part pkg:name="/word/media/image15.png" pkg:contentType="image/png">-->
<#--<pkg:binaryData>-->
<#--</pkg:binaryData>-->
<#--</pkg:part>-->
<#--<pkg:part pkg:name="/word/media/image8.png" pkg:contentType="image/png">-->
<#--<pkg:binaryData>-->
<#--</pkg:binaryData>-->
<#--</pkg:part>-->
<#--<pkg:part pkg:name="/word/media/image9.png" pkg:contentType="image/png">-->
<#--<pkg:binaryData>-->
<#--</pkg:binaryData>-->
<#--</pkg:part>-->

    <pkg:part pkg:name="/word/media/image2.png" pkg:contentType="image/png">
        <pkg:binaryData>${chart2}</pkg:binaryData>
    </pkg:part>
    <pkg:part pkg:name="/word/media/image3.png" pkg:contentType="image/png">
        <pkg:binaryData>${chart3}</pkg:binaryData>
    </pkg:part>
    <pkg:part pkg:name="/word/media/image4.png" pkg:contentType="image/png">
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
    <pkg:part pkg:name="/word/numbering.xml"
              pkg:contentType="application/vnd.openxmlformats-officedocument.wordprocessingml.numbering+xml">
        <pkg:xmlData>
            <w:numbering xmlns:wpc="http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas"
                         xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006"
                         xmlns:o="urn:schemas-microsoft-com:office:office"
                         xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships"
                         xmlns:m="http://schemas.openxmlformats.org/officeDocument/2006/math"
                         xmlns:v="urn:schemas-microsoft-com:vml"
                         xmlns:wp14="http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing"
                         xmlns:wp="http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing"
                         xmlns:w10="urn:schemas-microsoft-com:office:word"
                         xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main"
                         xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml"
                         xmlns:wpg="http://schemas.microsoft.com/office/word/2010/wordprocessingGroup"
                         xmlns:wpi="http://schemas.microsoft.com/office/word/2010/wordprocessingInk"
                         xmlns:wne="http://schemas.microsoft.com/office/word/2006/wordml"
                         xmlns:wps="http://schemas.microsoft.com/office/word/2010/wordprocessingShape"
                         mc:Ignorable="w14 wp14">
                <w:abstractNum w:abstractNumId="0">
                    <w:nsid w:val="5FE89DA3"/>
                    <w:multiLevelType w:val="singleLevel"/>
                    <w:tmpl w:val="5FE89DA3"/>
                    <w:lvl w:ilvl="0" w:tentative="0">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val=""/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="420" w:leftChars="0" w:hanging="420" w:firstLineChars="0"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default" w:ascii="Wingdings" w:hAnsi="Wingdings"/>
                        </w:rPr>
                    </w:lvl>
                </w:abstractNum>
                <w:abstractNum w:abstractNumId="1">
                    <w:nsid w:val="5FE94B91"/>
                    <w:multiLevelType w:val="singleLevel"/>
                    <w:tmpl w:val="5FE94B91"/>
                    <w:lvl w:ilvl="0" w:tentative="0">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val=""/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="420" w:leftChars="0" w:hanging="420" w:firstLineChars="0"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default" w:ascii="Wingdings" w:hAnsi="Wingdings"/>
                        </w:rPr>
                    </w:lvl>
                </w:abstractNum>
                <w:num w:numId="1">
                    <w:abstractNumId w:val="1"/>
                </w:num>
                <w:num w:numId="2">
                    <w:abstractNumId w:val="0"/>
                </w:num>
            </w:numbering>
        </pkg:xmlData>
    </pkg:part>
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
                <w:zoom w:percent="200"/>
                <w:doNotDisplayPageBoundaries w:val="1"/>
                <w:embedSystemFonts/>
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
                    <w:doNotExpandShiftReturn/>
                    <w:adjustLineHeightInTable/>
                    <w:doNotWrapTextWithPunct/>
                    <w:doNotUseEastAsianBreakRules/>
                    <w:useFELayout/>
                    <w:doNotUseIndentAsNumberingTabStop/>
                    <w:useAltKinsokuLineBreakRules/>
                    <w:compatSetting w:name="compatibilityMode" w:uri="http://schemas.microsoft.com/office/word"
                                     w:val="14"/>
                    <w:compatSetting w:name="overrideTableStyleFontSizeAndJustification"
                                     w:uri="http://schemas.microsoft.com/office/word" w:val="1"/>
                    <w:compatSetting w:name="enableOpenTypeFeatures" w:uri="http://schemas.microsoft.com/office/word"
                                     w:val="1"/>
                    <w:compatSetting w:name="doNotFlipMirrorIndents" w:uri="http://schemas.microsoft.com/office/word"
                                     w:val="1"/>
                </w:compat>
                <w:rsids>
                    <w:rsidRoot w:val="857524A0"/>
                    <w:rsid w:val="11BE017E"/>
                    <w:rsid w:val="1AF2C495"/>
                    <w:rsid w:val="1EF72E2B"/>
                    <w:rsid w:val="21B3A3F8"/>
                    <w:rsid w:val="2F9B112F"/>
                    <w:rsid w:val="32F77A19"/>
                    <w:rsid w:val="3312E023"/>
                    <w:rsid w:val="3DDB233C"/>
                    <w:rsid w:val="3FFD39E6"/>
                    <w:rsid w:val="46FFDF07"/>
                    <w:rsid w:val="4D7FDEE4"/>
                    <w:rsid w:val="4FFE7E10"/>
                    <w:rsid w:val="5EEFDB38"/>
                    <w:rsid w:val="5F5E84ED"/>
                    <w:rsid w:val="5F7BF0CA"/>
                    <w:rsid w:val="67DE0A9B"/>
                    <w:rsid w:val="67DFA838"/>
                    <w:rsid w:val="6BFFB298"/>
                    <w:rsid w:val="6F7AD141"/>
                    <w:rsid w:val="70A42E29"/>
                    <w:rsid w:val="73FF9520"/>
                    <w:rsid w:val="74EE848F"/>
                    <w:rsid w:val="76AF3956"/>
                    <w:rsid w:val="76D6B81F"/>
                    <w:rsid w:val="76F93956"/>
                    <w:rsid w:val="789C6CED"/>
                    <w:rsid w:val="7A7634CC"/>
                    <w:rsid w:val="7BA3673A"/>
                    <w:rsid w:val="7BEFA583"/>
                    <w:rsid w:val="7BFB8978"/>
                    <w:rsid w:val="7CFB0B52"/>
                    <w:rsid w:val="7D5E48F6"/>
                    <w:rsid w:val="7DFE2EC0"/>
                    <w:rsid w:val="7E8F1D21"/>
                    <w:rsid w:val="7F9F1051"/>
                    <w:rsid w:val="7F9F823B"/>
                    <w:rsid w:val="7FAF2F9B"/>
                    <w:rsid w:val="7FE66CB6"/>
                    <w:rsid w:val="7FFF67A4"/>
                    <w:rsid w:val="857524A0"/>
                    <w:rsid w:val="99DE736F"/>
                    <w:rsid w:val="AE7E3620"/>
                    <w:rsid w:val="B7BC0AAF"/>
                    <w:rsid w:val="BD79104A"/>
                    <w:rsid w:val="BDEEB61B"/>
                    <w:rsid w:val="BFBF2622"/>
                    <w:rsid w:val="BFDE2F6D"/>
                    <w:rsid w:val="BFF190E8"/>
                    <w:rsid w:val="CBFD5846"/>
                    <w:rsid w:val="D6379096"/>
                    <w:rsid w:val="D6FFC19F"/>
                    <w:rsid w:val="D7573ABF"/>
                    <w:rsid w:val="D7BEDDDF"/>
                    <w:rsid w:val="DDFBAD54"/>
                    <w:rsid w:val="DF7DAD0A"/>
                    <w:rsid w:val="DF9625AE"/>
                    <w:rsid w:val="DFDBD7E4"/>
                    <w:rsid w:val="E2CF7F96"/>
                    <w:rsid w:val="E7F44A40"/>
                    <w:rsid w:val="EC3714CA"/>
                    <w:rsid w:val="ECAFCEBB"/>
                    <w:rsid w:val="ECD7F1E0"/>
                    <w:rsid w:val="EDFDAF63"/>
                    <w:rsid w:val="EEA87678"/>
                    <w:rsid w:val="EFFF2CBF"/>
                    <w:rsid w:val="F57F8A55"/>
                    <w:rsid w:val="F7F75303"/>
                    <w:rsid w:val="F7FB0137"/>
                    <w:rsid w:val="FA79977C"/>
                    <w:rsid w:val="FBE7AFD5"/>
                    <w:rsid w:val="FCAD51F8"/>
                    <w:rsid w:val="FEFE7BDF"/>
                    <w:rsid w:val="FF7BAFFC"/>
                    <w:rsid w:val="FF9E7E4C"/>
                    <w:rsid w:val="FFBC80B5"/>
                    <w:rsid w:val="FFDF58A0"/>
                    <w:rsid w:val="FFE57553"/>
                    <w:rsid w:val="FFEB8C82"/>
                    <w:rsid w:val="FFF0FAED"/>
                    <w:rsid w:val="FFFB8037"/>
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
                    <w:lsdException w:qFormat="1" w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0"
                                    w:name="heading 1"/>
                    <w:lsdException w:qFormat="1" w:uiPriority="0" w:semiHidden="0" w:name="heading 2"/>
                    <w:lsdException w:qFormat="1" w:uiPriority="0" w:name="heading 3"/>
                    <w:lsdException w:qFormat="1" w:uiPriority="0" w:name="heading 4"/>
                    <w:lsdException w:qFormat="1" w:uiPriority="0" w:name="heading 5"/>
                    <w:lsdException w:qFormat="1" w:uiPriority="0" w:name="heading 6"/>
                    <w:lsdException w:qFormat="1" w:uiPriority="0" w:name="heading 7"/>
                    <w:lsdException w:qFormat="1" w:uiPriority="0" w:name="heading 8"/>
                    <w:lsdException w:qFormat="1" w:uiPriority="0" w:name="heading 9"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="index 1"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="index 2"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="index 3"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="index 4"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="index 5"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="index 6"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="index 7"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="index 8"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="index 9"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="toc 1"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="toc 2"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="toc 3"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="toc 4"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="toc 5"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="toc 6"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="toc 7"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="toc 8"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="toc 9"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Normal Indent"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="footnote text"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="annotation text"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="header"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="footer"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="index heading"/>
                    <w:lsdException w:qFormat="1" w:uiPriority="0" w:name="caption"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="table of figures"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="envelope address"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="envelope return"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="footnote reference"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0"
                                    w:name="annotation reference"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="line number"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="page number"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="endnote reference"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="endnote text"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0"
                                    w:name="table of authorities"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="macro"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="toa heading"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="List"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="List Bullet"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="List Number"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="List 2"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="List 3"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="List 4"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="List 5"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="List Bullet 2"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="List Bullet 3"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="List Bullet 4"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="List Bullet 5"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="List Number 2"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="List Number 3"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="List Number 4"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="List Number 5"/>
                    <w:lsdException w:qFormat="1" w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0"
                                    w:name="Title"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Closing"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Signature"/>
                    <w:lsdException w:qFormat="1" w:unhideWhenUsed="0" w:uiPriority="0"
                                    w:name="Default Paragraph Font"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Body Text"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Body Text Indent"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="List Continue"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="List Continue 2"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="List Continue 3"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="List Continue 4"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="List Continue 5"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Message Header"/>
                    <w:lsdException w:qFormat="1" w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0"
                                    w:name="Subtitle"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Salutation"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Date"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0"
                                    w:name="Body Text First Indent"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0"
                                    w:name="Body Text First Indent 2"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Note Heading"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Body Text 2"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Body Text 3"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Body Text Indent 2"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Body Text Indent 3"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Block Text"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Hyperlink"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="FollowedHyperlink"/>
                    <w:lsdException w:qFormat="1" w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0"
                                    w:name="Strong"/>
                    <w:lsdException w:qFormat="1" w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0"
                                    w:name="Emphasis"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Document Map"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Plain Text"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="E-mail Signature"/>
                    <w:lsdException w:qFormat="1" w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0"
                                    w:name="Normal (Web)"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="HTML Acronym"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="HTML Address"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="HTML Cite"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="HTML Code"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="HTML Definition"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="HTML Keyboard"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="HTML Preformatted"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="HTML Sample"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="HTML Typewriter"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="HTML Variable"/>
                    <w:lsdException w:qFormat="1" w:unhideWhenUsed="0" w:uiPriority="0" w:name="Normal Table"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="annotation subject"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table Simple 1"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table Simple 2"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table Simple 3"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table Classic 1"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table Classic 2"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table Classic 3"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table Classic 4"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table Colorful 1"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table Colorful 2"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table Colorful 3"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table Columns 1"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table Columns 2"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table Columns 3"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table Columns 4"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table Columns 5"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table Grid 1"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table Grid 2"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table Grid 3"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table Grid 4"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table Grid 5"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table Grid 6"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table Grid 7"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table Grid 8"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table List 1"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table List 2"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table List 3"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table List 4"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table List 5"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table List 6"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table List 7"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table List 8"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table 3D effects 1"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table 3D effects 2"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table 3D effects 3"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table Contemporary"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table Elegant"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table Professional"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table Subtle 1"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table Subtle 2"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table Web 1"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table Web 2"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table Web 3"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Balloon Text"/>
                    <w:lsdException w:qFormat="1" w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0"
                                    w:name="Table Grid"/>
                    <w:lsdException w:unhideWhenUsed="0" w:uiPriority="0" w:semiHidden="0" w:name="Table Theme"/>
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
                    <w:unhideWhenUsed/>
                    <w:qFormat/>
                    <w:uiPriority w:val="0"/>
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
                <w:style w:type="character" w:default="1" w:styleId="6">
                    <w:name w:val="Default Paragraph Font"/>
                    <w:semiHidden/>
                    <w:qFormat/>
                    <w:uiPriority w:val="0"/>
                </w:style>
                <w:style w:type="table" w:default="1" w:styleId="4">
                    <w:name w:val="Normal Table"/>
                    <w:semiHidden/>
                    <w:qFormat/>
                    <w:uiPriority w:val="0"/>
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
                    <w:name w:val="Normal (Web)"/>
                    <w:basedOn w:val="1"/>
                    <w:qFormat/>
                    <w:uiPriority w:val="0"/>
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
                <w:style w:type="table" w:styleId="5">
                    <w:name w:val="Table Grid"/>
                    <w:basedOn w:val="4"/>
                    <w:qFormat/>
                    <w:uiPriority w:val="0"/>
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
                            <a:srgbClr val="44546A"/>
                        </a:dk2>
                        <a:lt2>
                            <a:srgbClr val="E7E6E6"/>
                        </a:lt2>
                        <a:accent1>
                            <a:srgbClr val="5B9BD5"/>
                        </a:accent1>
                        <a:accent2>
                            <a:srgbClr val="ED7D31"/>
                        </a:accent2>
                        <a:accent3>
                            <a:srgbClr val="A5A5A5"/>
                        </a:accent3>
                        <a:accent4>
                            <a:srgbClr val="FFC000"/>
                        </a:accent4>
                        <a:accent5>
                            <a:srgbClr val="4472C4"/>
                        </a:accent5>
                        <a:accent6>
                            <a:srgbClr val="70AD47"/>
                        </a:accent6>
                        <a:hlink>
                            <a:srgbClr val="0563C1"/>
                        </a:hlink>
                        <a:folHlink>
                            <a:srgbClr val="954F72"/>
                        </a:folHlink>
                    </a:clrScheme>
                    <a:fontScheme name="Office">
                        <a:majorFont>
                            <a:latin typeface="Calibri Light"/>
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
                            <a:font script="Geor" typeface="Sylfaen"/>
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
                            <a:font script="Geor" typeface="Sylfaen"/>
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
                                            <a:lumMod val="110000"/>
                                            <a:satMod val="105000"/>
                                            <a:tint val="67000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                    <a:gs pos="50000">
                                        <a:schemeClr val="phClr">
                                            <a:lumMod val="105000"/>
                                            <a:satMod val="103000"/>
                                            <a:tint val="73000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                    <a:gs pos="100000">
                                        <a:schemeClr val="phClr">
                                            <a:lumMod val="105000"/>
                                            <a:satMod val="109000"/>
                                            <a:tint val="81000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                </a:gsLst>
                                <a:lin ang="5400000" scaled="0"/>
                            </a:gradFill>
                            <a:gradFill rotWithShape="1">
                                <a:gsLst>
                                    <a:gs pos="0">
                                        <a:schemeClr val="phClr">
                                            <a:satMod val="103000"/>
                                            <a:lumMod val="102000"/>
                                            <a:tint val="94000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                    <a:gs pos="50000">
                                        <a:schemeClr val="phClr">
                                            <a:satMod val="110000"/>
                                            <a:lumMod val="100000"/>
                                            <a:shade val="100000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                    <a:gs pos="100000">
                                        <a:schemeClr val="phClr">
                                            <a:lumMod val="99000"/>
                                            <a:satMod val="120000"/>
                                            <a:shade val="78000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                </a:gsLst>
                                <a:lin ang="5400000" scaled="0"/>
                            </a:gradFill>
                        </a:fillStyleLst>
                        <a:lnStyleLst>
                            <a:ln w="6350" cap="flat" cmpd="sng" algn="ctr">
                                <a:solidFill>
                                    <a:schemeClr val="phClr"/>
                                </a:solidFill>
                                <a:prstDash val="solid"/>
                                <a:miter lim="800000"/>
                            </a:ln>
                            <a:ln w="12700" cap="flat" cmpd="sng" algn="ctr">
                                <a:solidFill>
                                    <a:schemeClr val="phClr"/>
                                </a:solidFill>
                                <a:prstDash val="solid"/>
                                <a:miter lim="800000"/>
                            </a:ln>
                            <a:ln w="19050" cap="flat" cmpd="sng" algn="ctr">
                                <a:solidFill>
                                    <a:schemeClr val="phClr"/>
                                </a:solidFill>
                                <a:prstDash val="solid"/>
                                <a:miter lim="800000"/>
                            </a:ln>
                        </a:lnStyleLst>
                        <a:effectStyleLst>
                            <a:effectStyle>
                                <a:effectLst/>
                            </a:effectStyle>
                            <a:effectStyle>
                                <a:effectLst/>
                            </a:effectStyle>
                            <a:effectStyle>
                                <a:effectLst>
                                    <a:outerShdw blurRad="57150" dist="19050" dir="5400000" algn="ctr" rotWithShape="0">
                                        <a:srgbClr val="000000">
                                            <a:alpha val="63000"/>
                                        </a:srgbClr>
                                    </a:outerShdw>
                                </a:effectLst>
                            </a:effectStyle>
                        </a:effectStyleLst>
                        <a:bgFillStyleLst>
                            <a:solidFill>
                                <a:schemeClr val="phClr"/>
                            </a:solidFill>
                            <a:solidFill>
                                <a:schemeClr val="phClr">
                                    <a:tint val="95000"/>
                                    <a:satMod val="170000"/>
                                </a:schemeClr>
                            </a:solidFill>
                            <a:gradFill rotWithShape="1">
                                <a:gsLst>
                                    <a:gs pos="0">
                                        <a:schemeClr val="phClr">
                                            <a:tint val="93000"/>
                                            <a:satMod val="150000"/>
                                            <a:shade val="98000"/>
                                            <a:lumMod val="102000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                    <a:gs pos="50000">
                                        <a:schemeClr val="phClr">
                                            <a:tint val="98000"/>
                                            <a:satMod val="130000"/>
                                            <a:shade val="90000"/>
                                            <a:lumMod val="103000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                    <a:gs pos="100000">
                                        <a:schemeClr val="phClr">
                                            <a:shade val="63000"/>
                                            <a:satMod val="120000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                </a:gsLst>
                                <a:lin ang="5400000" scaled="0"/>
                            </a:gradFill>
                        </a:bgFillStyleLst>
                    </a:fmtScheme>
                </a:themeElements>
                <a:objectDefaults/>
            </a:theme>
        </pkg:xmlData>
    </pkg:part>
</pkg:package>