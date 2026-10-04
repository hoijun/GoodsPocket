# Events initial load failure

Conversation: https://chatgpt.com/c/6ac24c6a-c218-83ee-8ef0-990773c1ab8c
Reference: `design/references/events/load-failure.png`

Generate exactly ONE full-screen GoodsPocket EVENTS INITIAL LOAD FAILURE reference image, 393:852 portrait. Attached Events default.png defines the existing hierarchy; previously attached Home is the style anchor. Preserve centered 이벤트 title, current month 2026.04, 다가오는 하이라이트, filters 전체 / 발매 / 결제 마감 / 배송 / 오프라인 행사 and shared bottom navigation with Events selected and central orange +. Respect iPhone safe areas. Omit count pill because data has not loaded. Directly below filters replace event cards/timeline with ONE warm off-white error card, margin19px, radius10px, thin #EFEDEC border, almost no shadow. Center title 이벤트를 불러오지 못했어요, subtitle 잠시 후 다시 시도해 주세요., full-width orange 다시 시도 button. About166px tall, title15px bold, subtitle13px, button14px. No zero count, empty-result message, timeline, or error over header. Background #FFFCF8, orange #FF7445, ink #202838, muted #8A8F9B. No illustration, phone frame, collage, extra actions, calendar grid or external annotations. Retain existing header/filter/navigation geometry.

Generated support-state image; user visual approval pending. Normalized output has card around x17/y208/w359/h150. Preserve approved Events host geometry and 19dp margins rather than adopting generated header drift. Card internal contract: 12dp horizontal padding,35dp top,14dp bottom; title15sp/20sp,6dp gap,subtitle13sp/18sp,9dp gap,40dp minimum action height,8dp action radius. Card radius10dp,1dp border,no shadow.

Loading reuses this support card, replacing failure copy with 이벤트를 불러오는 중이에요 / 잠시만 기다려 주세요. and retry with progress. This is a state-only text/action substitution, not a new layout reference. Previously loaded events remain below feedback on refresh failure/retry; initial unknown data shows neither count nor empty result.
