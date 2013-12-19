<?xml version="1.0" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<table id="eventTable" border="1" cellspacing="1" cellpadding="5" width="90%">
			<tr style="background:black;">
				<th width="15%">
					<a id="Time" style="color:white; text-decoration:none;">Time</a>
				</th>
				<th width="5%">
					<a id="TraceId" style="color:white; text-decoration:none;">Thread id</a>
				</th>
				<th width="5%">
					<a id="Severity" style="color:white; text-decoration:none;">Severity</a>
				</th>
				<th width="50%">
					<a id="Message" style="color:white; text-decoration:none;">Message</a>
				</th>
				<th width="10%">
					<a id="Source" style="color:white; text-decoration:none;">Source Classs.Method</a>
				</th>
				<th width="15%">
					<a id="SubSystem" style="color:white; text-decoration:none;">SubSystem</a>
				</th>
			</tr>
			<xsl:for-each select="CommonBaseEvents/CommonBaseEvent">
				<tr>
					<xsl:choose>
						<xsl:when test="string(@creationTime)">
							<td>
								<xsl:value-of select="@creationTime" />
							</td>
						</xsl:when>
						<xsl:otherwise>
							<td>&#xA0;</td>
						</xsl:otherwise>
					</xsl:choose>

					<xsl:choose>
						<xsl:when test="string(sourceComponentId/@threadId)">
							<td>
								<xsl:value-of select="sourceComponentId/@threadId" />
							</td>
						</xsl:when>
						<xsl:otherwise>
							<td>&#xA0;</td>
						</xsl:otherwise>
					</xsl:choose>

					<xsl:choose>
						<xsl:when test="string(extendedDataElements[@name='Logging_Level']/values)">
							<td>
								<xsl:value-of select="extendedDataElements[@name='Logging_Level']/values" />
							</td>
						</xsl:when>
						<xsl:when
							test="string(extendedDataElements[@name='CommonBaseEventLogRecord:level']/children[@name='CommonBaseEventLogRecord:name']/values)"
						>
							<td>
								<xsl:value-of select="extendedDataElements[@name='CommonBaseEventLogRecord:level']/children[@name='CommonBaseEventLogRecord:name']/values" />
							</td>
						</xsl:when>
						<xsl:otherwise>
							<td>&#xA0;</td>
						</xsl:otherwise>
					</xsl:choose>

					<xsl:choose>
						<xsl:when test="string(@msg)">
							<td>
								<xsl:value-of select="@msg" />
								<xsl:if test="string(extendedDataElements[@name='CommonBaseEventLogRecord:multipleMessageValues'])">
									<xsl:for-each select="extendedDataElements[@name='CommonBaseEventLogRecord:multipleMessageValues']/values">
										<xsl:value-of select="." />
									</xsl:for-each>

								</xsl:if>
								<xsl:if test="string(extendedDataElements[@name='CommonBaseEventLogRecord:Exception'])">
									<i>
										<xsl:for-each select="extendedDataElements[@name='CommonBaseEventLogRecord:Exception']/values">
											<xsl:value-of select="." />
										</xsl:for-each>
									</i>
								</xsl:if>
							</td>
						</xsl:when>
						<xsl:otherwise>
							<td>&#xA0;</td>
						</xsl:otherwise>
					</xsl:choose>

					<xsl:choose>
						<xsl:when test="string(extendedDataElements[@name='CommonBaseEventLogRecord:sourceClassName']/values)">
							<td>
								<xsl:value-of select="extendedDataElements[@name='CommonBaseEventLogRecord:sourceClassName']/values" />
								<br />
								<xsl:value-of select="extendedDataElements[@name='CommonBaseEventLogRecord:sourceMethodName']/values" />
							</td>
						</xsl:when>
						<xsl:otherwise>
							<td>&#xA0;</td>
						</xsl:otherwise>
					</xsl:choose>



					<xsl:choose>
						<xsl:when test="string(sourceComponentId/@subComponent)">
							<td>
								<xsl:value-of select="sourceComponentId/@subComponent" />
							</td>
						</xsl:when>
						<xsl:otherwise>
							<td>&#xA0;</td>
						</xsl:otherwise>
					</xsl:choose>
				</tr>
			</xsl:for-each>
		</table>
	</xsl:template>
</xsl:stylesheet>
