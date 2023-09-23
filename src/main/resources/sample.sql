SELECT v1.ActuGetNo
	,v1.OtherNo
	,v1.GrpContNo
	,v1.PayMode
	,v1.SumGetMoney
	,v1.isFinRed
	,v1.PayModeName
	,nvl(lcgrpcont.ManageCom, v1.ManageCom)
	,nvl((
			SELECT name
			FROM ldcom
			WHERE comcode = lcgrpcont.ManageCom
			), v1.name)
	,nvl(v1.Drawer, lcgrpcont.grpname) AS Drawer
	,nvl(v1.DrawerID, lcgrpcont.appntno) AS DrawerID
	,v1.BankCode
	,v1.BankAccNo
	,1
	,v1.BackFeeFlag
	,CASE 
		WHEN EXISTS (
				SELECT 'w'
				FROM EBQApplyDetailInfo e
				WHERE e.EdorNo = v1.OtherNo
					AND e.grpcontno = v1.GrpContNo
				)
			THEN '网上保全'
		ELSE ''
		END EdorSource
	,(
		SELECT businessno
		FROM ljtempfeeclass
		WHERE tempfeeno = m.tempfeeno
		) Mcdbusinessno
FROM (
	SELECT a.ActuGetNo
		,a.OtherNo
		,a.OtherNoType
		,a.PayMode
		,a.SumGetMoney
		,(
			CASE 
				WHEN (
						a.SumGetMoney > 0
						AND (
							(
								length(a.OtherNo) = 10
								AND EXISTS (
									SELECT 1
									FROM ldtaxpayerinfo f
										,ljpbalancerela p
										,lcgrpcont g
									WHERE f.customerno = g.appntno
										AND g.grpcontno = p.grpcontno
										AND p.balancerelano = a.OtherNo
										AND f.customer_type = '01'
										AND f.TAXPAYER_FLAG = 'Y'
									)
								AND NOT EXISTS (
									SELECT 1
									FROM ljpbalancerela p
										,lcgrpcont g
									WHERE p.grpcontno = g.grpcontno
										AND g.grppoltype = '02'
										AND p.balancerelano = a.OtherNo
									)
								AND (
									SELECT sum(j.getmoney)
									FROM ljagetendorse j
										,ljpbalancerela p
									WHERE j.endorsementno = p.edorno
										AND p.balancerelano = a.OtherNo
										AND EXISTS (
											SELECT 1
											FROM lmriskapp_vms r
											WHERE r.riskcode = j.riskcode
												AND r.dutyfeeflag = 'Y'
											)
									) < 0
								AND EXISTS (
									SELECT 1
									FROM ljpbalancerela p
										,lcgrpcont g
									WHERE p.grpcontno = g.grpcontno
										AND p.balancerelano = a.OtherNo
										AND g.signdate >= DATE '2016-05-01'
									)
								AND EXISTS (
									SELECT 1
									FROM lmriskapp_vms r
										,ljpbalancerela p
										,lcgrppol c
									WHERE r.riskcode = c.riskcode
										AND p.balancerelano = a.OtherNo
										AND p.grpcontno = c.grpcontno
										AND r.dutyfeeflag = 'Y'
									)
								)
							OR (
								length(a.OtherNo) = 15
								AND substr(a.OtherNo, 13, 15) = '807'
								AND EXISTS (
									SELECT 1
									FROM ldtaxpayerinfo f
										,lpgrpedormain p
										,lcgrpcont g
									WHERE p.edorno = a.OtherNo
										AND p.grpcontno = g.grpcontno
										AND f.customerno = g.appntno
										AND f.customer_type = '01'
										AND f.TAXPAYER_FLAG = 'Y'
									)
								AND NOT EXISTS (
									SELECT 1
									FROM lpgrpedormain p
										,lcgrpcont g
									WHERE p.grpcontno = g.grpcontno
										AND g.grppoltype = '02'
										AND p.edorno = a.OtherNo
									)
								AND (
									SELECT sum(j.getmoney)
									FROM ljagetendorse j
									WHERE j.endorsementno = a.OtherNo
										AND EXISTS (
											SELECT 1
											FROM lmriskapp_vms r
											WHERE r.riskcode = j.riskcode
												AND r.dutyfeeflag = 'Y'
											)
									) < 0
								AND EXISTS (
									SELECT 1
									FROM lpgrpedormain p
										,lcgrpcont g
									WHERE p.grpcontno = g.grpcontno
										AND p.edorno = a.OtherNo
										AND g.signdate >= DATE '2016-05-01'
									)
								AND EXISTS (
									SELECT 1
									FROM lmriskapp_vms r
										,lpgrpedormain p
										,lcgrppol c
									WHERE r.riskcode = c.riskcode
										AND p.edorno = a.OtherNo
										AND p.grpcontno = c.grpcontno
										AND r.dutyfeeflag = 'Y'
									)
								)
							)
						)
					THEN '是'
				ELSE ' '
				END
			) isFinRed
		,(
			SELECT codename
			FROM ldcode
			WHERE codetype = 'getpaymode'
				AND code = a.PayMode
			) PayModeName
		,a.ManageCom
		,(
			SELECT name
			FROM ldcom
			WHERE comcode = a.ManageCom
			) name
		,a.Drawer
		,a.DrawerID
		,a.BankCode
		,a.BankAccNo
		,nvl((
				SELECT DISTINCT grpcontno
				FROM v_grp_grpcontnootherno
				WHERE otherno = a.otherno
				), a.otherno) AS grpcontno
		,a.BackFeeFlag
		,makedate
	FROM LJAGet a
	WHERE a.operstate = '0'
		AND NOT EXISTS (
			SELECT 1
			FROM UPSReceive
			WHERE ActuGetNo = a.ActuGetNo
			)
		AND NOT EXISTS (
			SELECT 1
			FROM ljfiget
			WHERE actugetno = a.ActuGetNo
			)
		AND NOT EXISTS (
			SELECT 1
			FROM ljagetother
			WHERE actugetno = a.ActuGetNo
				AND OtherNoType = '15'
				AND enteraccdate IS NULL
			)
		AND NOT EXISTS (
			SELECT 1
			FROM ljagetother
			WHERE actugetno = a.ActuGetNo
				AND OtherNoType = '8'
			)
		AND (
			a.balanceontime = '5'
			OR a.balanceontime IS NULL
			)
		AND a.OtherNo = '000052360909807'
	) v1
LEFT OUTER JOIN lcgrpcont ON lcgrpcont.grpcontno = v1.grpcontno
LEFT OUTER JOIN ljagettempfee m ON v1.ActuGetNo = m.actugetno
	AND EXISTS (
		SELECT 'X'
		FROM mcdorderassociate
		WHERE businessstate = '2'
			AND businessno = (
				SELECT businessno
				FROM ljtempfeeclass
				WHERE tempfeeno = m.tempfeeno
				)
		)
WHERE v1.OtherNoType != '13'
	AND NOT EXISTS (
		SELECT 'X'
		FROM EBQImportLog a
			,EBQApplyDetailInfo b
		WHERE a.applyno = b.applyno
			AND b.edorno = v1.OtherNo
			AND a.STATE = '0'
			AND FILEDMISSION = '006'
		)
ORDER BY v1.makedate DESC
