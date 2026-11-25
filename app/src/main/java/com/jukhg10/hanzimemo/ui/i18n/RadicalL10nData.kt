package com.jukhg10.hanzimemo.ui.i18n

import com.jukhg10.hanzimemo.data.preferences.AppLanguage
import com.jukhg10.hanzimemo.domain.models.EnhancedCharacterInfo

/**
 * Catálogo lingüístico pericial de radicales Kangxi, componentes estructurales
 * y motor de localización semántica para definiciones CEDICT y fórmulas pedagógicas.
 */
object RadicalL10nData {

    data class RadicalEntry(
        val es: String,
        val en: String,
        val zh: String
    )

    // Catálogo canónico de los 214 radicales Kangxi y sus variantes más frecuentes
    private val radicalMap: Map<String, RadicalEntry> = mapOf(
        "一" to RadicalEntry("uno, unidad", "one, single", "单一，初始"),
        "丨" to RadicalEntry("línea vertical", "vertical stroke", "竖画，贯通"),
        "丶" to RadicalEntry("punto", "dot", "点，顿析"),
        "丿" to RadicalEntry("trazo diagonal descendente a la izquierda", "slash, falling left", "撇画，下垂"),
        "乙" to RadicalEntry("segundo, gancho", "second, hook", "弯钩，曲折"),
        "乛" to RadicalEntry("gancho", "hook", "横钩"),
        "亅" to RadicalEntry("gancho vertical", "hooked vertical", "竖钩"),
        "二" to RadicalEntry("dos", "two", "双数，天地"),
        "亠" to RadicalEntry("tapa superior", "lid, top", "文字头，覆盖"),
        "人" to RadicalEntry("persona, individuo", "person, human", "人部，立人"),
        "亻" to RadicalEntry("persona (radical lateral)", "person radical", "单人旁"),
        "儿" to RadicalEntry("persona, piernas, hijo", "son, legs, person", "儿部，挺立人体"),
        "入" to RadicalEntry("entrar, ingresar", "enter", "入部，内敛"),
        "八" to RadicalEntry("ocho, división", "eight, separate", "八字旁，分散"),
        "丷" to RadicalEntry("ocho (variante superior)", "eight (top)", "倒八字"),
        "冂" to RadicalEntry("frontera, periferia", "border, box", "同字框，远郊"),
        "冖" to RadicalEntry("cubierta, velo", "crown, cover", "秃宝盖，遮蔽"),
        "冫" to RadicalEntry("hielo, frío", "ice, cold", "两点水，冰冻"),
        "几" to RadicalEntry("mesa pequeña, varios", "table, small", "几案，几部"),
        "凵" to RadicalEntry("recipiente, fosa", "open container", "凹字框，器具"),
        "刀" to RadicalEntry("cuchillo, corte, filo", "knife, sword, cut", "刀部，利刃"),
        "刂" to RadicalEntry("cuchillo (radical lateral)", "knife (side)", "立刀旁，切割"),
        "力" to RadicalEntry("fuerza, poder, esfuerzo", "power, strength", "力部，筋力劳作"),
        "勹" to RadicalEntry("envolver, paquete", "wrap, embrace", "包字头，环抱"),
        "匕" to RadicalEntry("cuchara, daga", "spoon, dagger", "匕首，匙勺"),
        "匚" to RadicalEntry("caja, recipiente lateral", "box, basket", "三框栏，容纳"),
        "匸" to RadicalEntry("esconder, refugio", "hiding place", "隐蔽处"),
        "十" to RadicalEntry("diez, completitud", "ten, completeness", "十字旁，圆满"),
        "卜" to RadicalEntry("adivinación, oráculo", "divination, oracle", "卜卦，龟甲兆纹"),
        "卩" to RadicalEntry("sello, arrodillarse", "seal, kneeling person", "单耳刀，节制"),
        "⺋" to RadicalEntry("sello (variante)", "seal variant", "单耳旁"),
        "厂" to RadicalEntry("acantilado, fábrica", "cliff, slope", "厂字头，山崖"),
        "厶" to RadicalEntry("privado, individual", "private, secret", "私人，私曲"),
        "又" to RadicalEntry("mano derecha, además", "right hand, also", "右手，反复"),
        "口" to RadicalEntry("boca, abertura, lenguaje", "mouth, opening, speech", "口部，语言器官"),
        "囗" to RadicalEntry("cercado, envolvente", "enclosure, surround", "大口框，城墙闭合"),
        "土" to RadicalEntry("tierra, suelo, polvo", "earth, soil, ground", "土部，泥土大地"),
        "士" to RadicalEntry("erudito, guerrero, caballero", "scholar, gentleman", "士大夫，文士"),
        "夂" to RadicalEntry("ir despacio, seguir", "walk slowly, follow", "缓步，跟随"),
        "夊" to RadicalEntry("caminar lento", "slow walk", "折足缓行"),
        "夕" to RadicalEntry("tarde, anochecer, luna creciente", "evening, sunset", "夕阳，薄暮"),
        "大" to RadicalEntry("grande, noble", "big, great, large", "大部，人形舒展"),
        "女" to RadicalEntry("mujer, femenino", "woman, female", "女部，母性柔和"),
        "子" to RadicalEntry("hijo, niño, semilla", "child, son, seed", "子嗣，后代"),
        "宀" to RadicalEntry("techo, hogar, protección", "roof, house, shelter", "宝盖头，居所"),
        "寸" to RadicalEntry("pulgada, medida, regla", "inch, measure, rule", "寸部，指脉分寸"),
        "小" to RadicalEntry("pequeño, diminuto", "small, tiny", "小部，微细"),
        "⺌" to RadicalEntry("pequeño (superior)", "small (top)", "小字头"),
        "尢" to RadicalEntry("cojo, curvado", "lame, bent", "曲胫，异形"),
        "尸" to RadicalEntry("cuerpo, cadáver, asiento", "corpse, body", "尸字旁，人卧形"),
        "屮" to RadicalEntry("brote vegetal, germinar", "sprout, bud", "草木发芽"),
        "山" to RadicalEntry("montaña, cumbre", "mountain, cliff", "山峰，崇山峻岭"),
        "川" to RadicalEntry("río, corriente de agua", "river, stream", "江河，水流川流"),
        "巛" to RadicalEntry("corriente fluvial", "flowing river", "川部三弯"),
        "工" to RadicalEntry("trabajo, herramienta, artesanía", "work, craft, tool", "巧匠，器具工具"),
        "己" to RadicalEntry("uno mismo", "self", "自己，身形"),
        "已" to RadicalEntry("ya, terminar", "already, finish", "已然，终了"),
        "巳" to RadicalEntry("serpiente, feto", "snake, fetus", "巳时，生肖蛇"),
        "巾" to RadicalEntry("pañuelo, tela, tejido", "towel, cloth, scarf", "布帛，织品"),
        "干" to RadicalEntry("secar, escudo, intervenir", "shield, stem, dry", "兵戈盾牌，天干"),
        "幺" to RadicalEntry("pequeño, fino, hilo", "tiny, short, thread", "幼小，细微"),
        "广" to RadicalEntry("edificio en pendiente, amplio", "broad shelter, shed", "广字旁，房屋庭堂"),
        "廴" to RadicalEntry("paso largo, avance", "long stride, walk", "建字底，迈步延展"),
        "廾" to RadicalEntry("dos manos levantadas", "hands joined, clasp", "双手恭奉"),
        "弋" to RadicalEntry("flecha arrojadiza, estaca", "arrow, stake, peg", "射鸟箭矢"),
        "弓" to RadicalEntry("arco, curvatura", "bow, curve", "弧弓，弯曲"),
        "彐" to RadicalEntry("hocico de animal", "snout, animal head", "山猪头，锐鼻"),
        "彑" to RadicalEntry("cabeza de cerdo salvaje", "pig head", "彘首"),
        "彡" to RadicalEntry("pelo, plumas, brillo estilizado", "hair, pattern, stripes", "三撇，文采花纹"),
        "彳" to RadicalEntry("paso con pie izquierdo, avanzar", "step left, proceed", "双人旁，街道道路"),
        "心" to RadicalEntry("corazón, mente, emoción", "heart, mind, feelings", "心部，心意情感"),
        "忄" to RadicalEntry("corazón (radical vertical)", "heart (vertical)", "竖心旁，内心情感"),
        "戈" to RadicalEntry("alabarda, arma, combate", "spear, halberd, weapon", "干戈，古代兵器"),
        "户" to RadicalEntry("puerta de una sola hoja", "door, household", "门户，单扇门"),
        "戶" to RadicalEntry("puerta, casa", "door, family", "门扇"),
        "手" to RadicalEntry("mano, destreza, acción manual", "hand, action, craft", "手部，动作技巧"),
        "扌" to RadicalEntry("mano (radical de acción)", "hand (action)", "提手旁，手动之势"),
        "支" to RadicalEntry("rama, sostener, división", "branch, support", "分支，手持枝节"),
        "攴" to RadicalEntry("golpear ligeramente, instruir", "tap, knock, strike", "轻击，持杖施教"),
        "攵" to RadicalEntry("golpear suavemente (variante)", "strike variant", "反文旁，修整文教"),
        "文" to RadicalEntry("cultura, escritura, adorno", "literature, culture, pattern", "文字，文雅文明"),
        "斗" to RadicalEntry("cucharón, medida de grano", "dipper, measure", "量斗，斗室"),
        "斤" to RadicalEntry("hacha, peso tradicional (jin)", "axe, pound", "斤两，斧斤"),
        "方" to RadicalEntry("cuadrado, dirección, método", "square, direction, method", "方正，方向"),
        "无" to RadicalEntry("nada, no tener, sin", "not, without", "无形，无常"),
        "旡" to RadicalEntry("asfixia, falta de aire", "choke, lack", "气竭"),
        "日" to RadicalEntry("sol, día, luz solar", "sun, day, light", "日部，太阳光辉"),
        "曰" to RadicalEntry("decir, hablar, declarar", "say, speak", "曰言，子曰"),
        "月" to RadicalEntry("luna, mes, cuerpo de carne", "moon, month, flesh", "月部，月轮肉形"),
        "木" to RadicalEntry("árbol, madera, bosque", "tree, wood", "木部，草木生发"),
        "欠" to RadicalEntry("bostezar, deber, adeudar", "yawn, lack, owe", "呵欠，短缺"),
        "止" to RadicalEntry("detenerse, pie firme", "stop, foot", "止步，驻留"),
        "歹" to RadicalEntry("muerte, decadencia, malvado", "death, bad, decay", "残骨，衰亡"),
        "殳" to RadicalEntry("maza de madera, golpear", "cudgel, strike", "持殳，击伐"),
        "毋" to RadicalEntry("no hacer, prohibición, madre", "do not, mother", "毋宁，禁阻"),
        "比" to RadicalEntry("comparar, competir", "compare, match", "并肩对比"),
        "毛" to RadicalEntry("pelo, pelaje, pluma", "fur, hair, feather", "毛发，毫毛"),
        "氏" to RadicalEntry("clan, linaje, familia", "clan, family", "宗族氏族"),
        "气" to RadicalEntry("vapor, aire, energía Qi", "steam, breath, Qi energy", "气华，云蒸霞蔚"),
        "水" to RadicalEntry("agua, líquido, río", "water, liquid, river", "水部，润下滋生"),
        "氵" to RadicalEntry("agua (tres gotas)", "water radical (three dots)", "三点水，水流汇聚"),
        "火" to RadicalEntry("fuego, llama, calor", "fire, flame, heat", "火部，烈焰升腾"),
        "灬" to RadicalEntry("fuego (cuatro puntos inferiores)", "fire (bottom dots)", "四点底，炭火炽热"),
        "爪" to RadicalEntry("garra, uña, asir", "claw, talon, grasp", "鸟爪，爪牙"),
        "爫" to RadicalEntry("garra (variante superior)", "claw (top)", "采爪头"),
        "父" to RadicalEntry("padre, patriarca", "father", "父亲，家长"),
        "爻" to RadicalEntry("diagrama del I-Ching, entrelazado", "intertwined, hexagram line", "交错爻象"),
        "爿" to RadicalEntry("tabla de madera, cama", "wooden split, bed", "木片，床榻"),
        "片" to RadicalEntry("astilla, lámina, tarjeta", "slice, sheet, flake", "破片，薄片"),
        "牙" to RadicalEntry("colmillo, diente", "fang, tooth", "利牙，象牙"),
        "牛" to RadicalEntry("buey, vaca, res", "ox, cow, bull", "牛部，勤劳稳健"),
        "牜" to RadicalEntry("buey (radical lateral)", "ox (side)", "牛字旁"),
        "犬" to RadicalEntry("perro, cánido", "dog, hound", "家犬，猎犬"),
        "犭" to RadicalEntry("perro / fiera (radical lateral)", "beast / animal radical", "反犬旁，猛兽野性"),
        "玄" to RadicalEntry("oscuro, misterioso, profundo", "dark, profound, mysterious", "玄妙，深远"),
        "玉" to RadicalEntry("jade, gema preciosa", "jade, precious stone", "美玉，莹润"),
        "王" to RadicalEntry("rey, jade (en caracteres)", "king, jade", "王者，王字旁（多指美玉）"),
        "瓜" to RadicalEntry("melón, calabaza, fruto rastrero", "melon, gourd", "瓜果，蔓生"),
        "瓦" to RadicalEntry("teja de barro, cerámica", "tile, pottery", "砖瓦，烧陶"),
        "甘" to RadicalEntry("dulce, agradable, deleite", "sweet, pleasant", "甘甜，美味"),
        "生" to RadicalEntry("nacer, vida, brote", "birth, life, grow", "生生不息，初萌"),
        "用" to RadicalEntry("usar, emplear, utilidad", "use, apply", "运用，功用"),
        "田" to RadicalEntry("campo de cultivo, tierra agrícola", "field, farmland", "田野，耕织"),
        "疋" to RadicalEntry("pieza de tela, pierna", "cloth bolt, foot", "疋布，下肢"),
        "疒" to RadicalEntry("enfermedad, patología", "sickness, disease", "病字旁，疾痛休养"),
        "癶" to RadicalEntry("pies en divergencia, caminar", "divergent legs, footstep", "双足发力"),
        "白" to RadicalEntry("blanco, claro, pureza", "white, pure, clear", "白雪，明晰"),
        "皮" to RadicalEntry("piel, cuero, corteza", "skin, leather, peel", "毛皮，表皮"),
        "皿" to RadicalEntry("plato, cuenco, vasija", "dish, vessel, bowl", "器皿，盘盂"),
        "目" to RadicalEntry("ojo, visión, clasificación", "eye, vision", "目部，察视"),
        "矛" to RadicalEntry("lanza, arpón, ataque", "spear, lance", "长矛，锐刺"),
        "矢" to RadicalEntry("flecha, puntería, rectitud", "arrow, aim", "箭矢，端的"),
        "石" to RadicalEntry("piedra, roca, mineral", "stone, rock", "岩石，坚硬"),
        "示" to RadicalEntry("manifestar, divinidad, ancestro", "altar, spirit, show", "神示，祖庙祭祀"),
        "礻" to RadicalEntry("divinidad, bendición (radical)", "spirit / blessing radical", "示字旁，祈福吉庆"),
        "禸" to RadicalEntry("huella de animal salvaje", "beast footprint", "兽足践踏"),
        "禾" to RadicalEntry("cereal, grano, espiga de trigo", "grain, cereal, seedling", "禾苗，稻穗谷物"),
        "穴" to RadicalEntry("cueva, caverna, orificio", "cave, hole, den", "穴居，空洞"),
        "立" to RadicalEntry("estar de pie, erigirse, fundar", "stand, establish", "矗立，建立"),
        "竹" to RadicalEntry("bambú, caña flexible", "bamboo, flexibility", "翠竹，青竿"),
        "⺮" to RadicalEntry("bambú (variante superior)", "bamboo (top)", "竹字头，竹编器物"),
        "米" to RadicalEntry("arroz, grano, alimento", "rice, grain, seed", "米粮，白米"),
        "糸" to RadicalEntry("hilo fino, seda, cuerda", "silk thread, cord", "丝线，连络"),
        "纟" to RadicalEntry("seda / hilo (radical simplificado)", "silk radical (simp)", "绞丝旁，经纬编织"),
        "缶" to RadicalEntry("cántaro de barro, ánfora", "earthen jar, amphora", "陶缶，瓦罐"),
        "网" to RadicalEntry("red, malla, entramado", "net, mesh, network", "罗网，羁绊"),
        "罒" to RadicalEntry("red (variante superior)", "net (top)", "四字头，纲纪网罗"),
        "羊" to RadicalEntry("oveja, cabra, bondad", "sheep, goat, auspicious", "吉祥，吉羊"),
        "羽" to RadicalEntry("plumas, ala, vuelo", "feather, wing, plumage", "羽翼，飞腾"),
        "老" to RadicalEntry("anciano, vejez, venerable", "old, venerable, aged", "长者，久远"),
        "而" to RadicalEntry("barba, y, además", "beard, and, yet", "须髯，语助连词"),
        "耒" to RadicalEntry("arado de madera, agricultura", "plow, tillage", "农具，翻土"),
        "耳" to RadicalEntry("oreja, oído, escuchar", "ear, hearing", "耳部，聆听"),
        "聿" to RadicalEntry("pincel de caligrafía, escribir", "writing brush, style", "手持毛笔"),
        "肉" to RadicalEntry("carne, tejido muscular", "meat, flesh", "肉身，肌肤"),
        "⺼" to RadicalEntry("carne / cuerpo (radical de órganos)", "flesh radical (organs)", "月字旁（肉部），脏腑机体"),
        "臣" to RadicalEntry("ministro, siervo, lealtad", "official, minister", "人臣，俯首忠顺"),
        "自" to RadicalEntry("nariz, uno mismo, desde", "self, nose, from", "自身，源起"),
        "至" to RadicalEntry("llegar, alcanzar, extremo", "arrive, reach, ultimate", "到达，至极"),
        "臼" to RadicalEntry("mortero, triturar grano", "mortar, mill", "石臼，舂米"),
        "舌" to RadicalEntry("lengua, gusto, dicción", "tongue, taste, speech", "舌苔，言辞"),
        "舛" to RadicalEntry("pies cruzados, error, discordia", "feet crossed, mistake", "背道而驰"),
        "舟" to RadicalEntry("bote, canoa, navegación", "boat, ship, vessel", "舟楫，渡江"),
        "艮" to RadicalEntry("obstáculo, firmeza, quietud", "tough, stop, mountain trigram", "艮卦，止而不动"),
        "色" to RadicalEntry("color, apariencia, deseo", "color, look, desire", "色彩，容颜"),
        "艸" to RadicalEntry("hierba, brotes verdes", "grass, herbs", "百草丛生"),
        "艹" to RadicalEntry("hierba / vegetación (superior)", "grass / plant radical (top)", "草字头，草木花草"),
        "虍" to RadicalEntry("rayas de tigre, fiereza", "tiger stripes, ferocious", "虎皮花纹"),
        "虫" to RadicalEntry("insecto, gusano, reptil", "insect, worm, reptile", "虫豸，昆虫百类"),
        "血" to RadicalEntry("sangre, linaje, sacrificio", "blood, lineage", "精血，祭血"),
        "行" to RadicalEntry("caminar, encrucijada, conducta", "walk, crossroads, act", "通行，道义行动"),
        "衣" to RadicalEntry("ropa, vestimenta, túnica", "clothes, garment, coat", "衣裳，护体"),
        "衤" to RadicalEntry("ropa (radical lateral)", "clothes radical (side)", "衣字旁，纺织缝纫"),
        "襾" to RadicalEntry("cubierta superior, oeste", "cover, west", "遮掩覆物"),
        "见" to RadicalEntry("ver, observar, percibir", "see, meet, observe", "相见，明察"),
        "角" to RadicalEntry("cuerno, esquina, ángulo", "horn, corner, angle", "兽角，角落"),
        "言" to RadicalEntry("palabra, lenguaje, discurso", "speech, words, language", "言论，辞章"),
        "讠" to RadicalEntry("lenguaje / habla (radical)", "speech radical (simp)", "言字旁，陈说论辩"),
        "谷" to RadicalEntry("valle, barranco, arroyo", "valley, gorge", "幽谷，泉流"),
        "豆" to RadicalEntry("frijol, legumbre, copa ritual", "bean, ritual cup", "大豆，礼器豆具"),
        "豕" to RadicalEntry("cerdo, puerco", "pig, boar", "豕部，家猪"),
        "豸" to RadicalEntry("bestia sin patas, alimaña", "beast, predator", "猛兽探身"),
        "贝" to RadicalEntry("concha cauri, dinero, riqueza", "cowrie shell, wealth, money", "海贝，资财货币"),
        "赤" to RadicalEntry("rojo intenso, sinceridad, fuego", "red, crimson, sincere", "赤子之心，赤红"),
        "走" to RadicalEntry("caminar rápido, correr, marchar", "walk, run, depart", "奔走，步履"),
        "辶" to RadicalEntry("caminar / avance (radical inferior)", "walk / move radical", "走之底，迁徙行进"),
        "足" to RadicalEntry("pie, pierna, suficiente", "foot, leg, enough", "足下，完满"),
        "⻊" to RadicalEntry("pie (radical lateral)", "foot radical (side)", "足字旁，蹴踢蹬踩"),
        "身" to RadicalEntry("cuerpo, torso, embarazada", "body, torso, pregnancy", "躯干，身怀六甲"),
        "车" to RadicalEntry("carro, vehículo, rueda", "cart, car, vehicle", "舆车，行辕"),
        "辛" to RadicalEntry("picante, sufrimiento, cincel", "spicy, bitter, suffering", "辛苦，辛味"),
        "辰" to RadicalEntry("tiempo, constelación, dragón", "morning, celestial sign, dragon", "辰星，时令"),
        "辵" to RadicalEntry("correr y detenerse, marcha", "walk and halt", "辵部行进"),
        "邑" to RadicalEntry("ciudad amurallada, feudo", "city, town, domain", "城邑，邑都"),
        "阝" to RadicalEntry("colina (izquierda) / ciudad (derecha)", "hill (left) / city (right)", "双耳旁，左山岭右城邑"),
        "酉" to RadicalEntry("ánfora de vino, licor, fermento", "wine vessel, liquor, ferment", "酒樽，醇酿"),
        "采" to RadicalEntry("recolectar, elegir, brillo", "pluck, gather, pick", "采摘，采纳"),
        "里" to RadicalEntry("aldea, distancia china, interior", "village, mile, inside", "里巷，邻里"),
        "金" to RadicalEntry("metal, oro, brillo áureo", "metal, gold, money", "金属，真金"),
        "钅" to RadicalEntry("metal / herramientas (radical)", "metal radical (simp)", "金字旁，锋刃钱币"),
        "长" to RadicalEntry("largo, cabellera, mayor", "long, leader, grow", "长久，长生"),
        "门" to RadicalEntry("puerta, portal de dos hojas", "gate, door, school", "大门，师承门户"),
        "阜" to RadicalEntry("colina escalonada, montículo", "mound, hill", "土丘厚实"),
        "隶" to RadicalEntry("esclavo, sirviente, captura", "servant, capture", "追捕奴仆"),
        "隹" to RadicalEntry("ave de cola corta, pájaro", "short-tailed bird", "飞禽小雀"),
        "雨" to RadicalEntry("lluvia, nubes pluviales", "rain, cloud, weather", "甘霖，时雨"),
        "青" to RadicalEntry("azul verdoso, juventud, pureza", "blue-green, young, pure", "青春，苍翠"),
        "非" to RadicalEntry("no, error, contradicción", "not, wrong, against", "是非，非然"),
        "面" to RadicalEntry("rostro, superficie, lado", "face, surface, side", "面庞，表层"),
        "革" to RadicalEntry("cuero curtido, transformación", "leather, transform, change", "制革，变革"),
        "韦" to RadicalEntry("cuero blando, rodear", "tanned leather, encircle", "柔皮包合"),
        "韭" to RadicalEntry("puerro, hortaliza perenne", "leek, chives", "韭菜生发"),
        "音" to RadicalEntry("sonido musical, tono, noticia", "sound, tone, music", "乐音，声律"),
        "页" to RadicalEntry("cabeza, página, folio", "head, page, leaf", "书页，头额"),
        "风" to RadicalEntry("viento, brisa, estilo", "wind, breeze, style", "清风，风度"),
        "飞" to RadicalEntry("volar, alado, velocidad", "fly, speed, flutter", "腾飞，翔舞"),
        "食" to RadicalEntry("comida, alimento, comer", "food, eat, meal", "饱食佳肴"),
        "饣" to RadicalEntry("comida / alimento (radical)", "food radical (simp)", "饣字旁，膳食饮食"),
        "首" to RadicalEntry("cabeza, primero, líder", "head, first, leader", "首要第一，头颅"),
        "香" to RadicalEntry("fragancia, aroma agradable", "fragrance, aroma, scent", "馨香谷气"),
        "马" to RadicalEntry("caballo, velocidad", "horse, steed, fast", "骏马驰骋"),
        "骨" to RadicalEntry("hueso, estructura ósea, firmeza", "bone, skeleton, frame", "骨骼立架"),
        "高" to RadicalEntry("alto, elevado, noble", "tall, high, lofty", "崇高楼阁"),
        "髟" to RadicalEntry("cabello largo, cabellera", "long hair, locks", "长发披拂"),
        "鬥" to RadicalEntry("luchar, combate", "fight, struggle", "对仗搏击"),
        "鬯" to RadicalEntry("vino sacrificial con hierbas", "sacrificial wine", "郁金香草酒"),
        "鬲" to RadicalEntry("trípode de caldero de barro", "cauldron, tripod", "三足陶鼎"),
        "鬼" to RadicalEntry("espíritu, fantasma, demonio", "ghost, spirit, demon", "鬼魅神思"),
        "鱼" to RadicalEntry("pez, pesca, criatura acuática", "fish, seafood, aquatic", "游鱼生鲜"),
        "鸟" to RadicalEntry("pájaro, ave voladora", "bird, avian", "飞禽羽鸟"),
        "卤" to RadicalEntry("salmuera, tierra salina", "salt, alkaline soil", "卤水卤汁"),
        "鹿" to RadicalEntry("ciervo, venado", "deer, stag", "呦呦鹿鸣"),
        "麦" to RadicalEntry("trigo, cebada, cereal", "wheat, barley", "麦浪秋收"),
        "麻" to RadicalEntry("cáñamo, lino", "hemp, flax", "麻织纤维"),
        "黄" to RadicalEntry("amarillo, color imperial", "yellow, imperial color", "大地黄色，尊贵"),
        "黍" to RadicalEntry("mijo glutinoso", "millet, grain", "黍谷良实"),
        "黑" to RadicalEntry("negro, hollín, oscuridad", "black, dark, soot", "焦烟深黑"),
        "黹" to RadicalEntry("bordado, aguja e hilo", "embroidery, needlework", "针黹刺绣"),
        "黾" to RadicalEntry("rana, sapo, anfibio", "frog, toad, amphibian", "蛙蛙黾勉"),
        "鼎" to RadicalEntry("caldero ceremonial, trípode", "tripod cauldron, vessel", "重鼎国之重器"),
        "鼓" to RadicalEntry("tambor, ritmo ceremonial", "drum, beat, percussion", "战鼓乐鸣"),
        "鼠" to RadicalEntry("ratón, roedor", "mouse, rat, rodent", "子鼠敏捷"),
        "鼻" to RadicalEntry("nariz, respiración", "nose, smell, breath", "鼻息相通"),
        "齐" to RadicalEntry("ordenado, alineado, completo", "neat, equal, uniform", "整齐划一"),
        "齿" to RadicalEntry("dientes, edad, orden", "tooth, dental, age", "齿轮牙齿，列序"),
        "龙" to RadicalEntry("dragón chino, majestuosidad", "dragon, imperial", "华夏神龙，腾跃"),
        "龟" to RadicalEntry("tortuga, longevidad", "turtle, tortoise", "灵龟长寿"),
        "龠" to RadicalEntry("flauta de bambú, armonía", "flute, music tube", "编管乐律"),

        // Componentes fonéticos y estructurales comunes
        "尔" to RadicalEntry("tú (pronombre arcaico), cercano", "you (archaic pronoun), close", "尔（你，祖传代词）"),
        "安" to RadicalEntry("paz, sosiego, tranquilidad", "peace, safe, tranquil", "安宁，平安"),
        "可" to RadicalEntry("apto, posible, permitir", "able, may, permit", "可以，赞同"),
        "巴" to RadicalEntry("aferrarse, anhelar", "cling to, yearn for", "巴结，期盼"),
        "也" to RadicalEntry("también, asimismo", "also, too", "也是，语助词"),
        "包" to RadicalEntry("envoltura, contener", "package, contain", "包裹，囊括"),
        "中" to RadicalEntry("centro, medio, interior", "middle, center, within", "中央，正中"),
        "丁" to RadicalEntry("cuarto elemento, clavo, varón", "nail, fourth, man", "丁火，壮丁"),
        "太" to RadicalEntry("demasiado, sumo, supremo", "greatest, too much, supreme", "太极，至尊"),
        "平" to RadicalEntry("plano, en paz, nivelado", "flat, peaceful, level", "平坦，安定"),
        "古" to RadicalEntry("antiguo, pretérito, venerable", "ancient, old, former", "古往今来，久远"),
        "台" to RadicalEntry("plataforma, base, terraza", "platform, stage, terrace", "台阶，基坛"),
        "同" to RadicalEntry("igual, juntos, armonía", "same, together, united", "同心，相同"),
        "吉" to RadicalEntry("auspicioso, favorable, dicha", "lucky, auspicious, fortune", "吉庆吉祥"),
        "寺" to RadicalEntry("templo, disciplina budista", "temple, monastery, court", "寺院，法度"),
        "分" to RadicalEntry("dividir, separar, porción", "divide, split, portion", "分割，辨别"),
        "半" to RadicalEntry("mitad, medio, parcial", "half, semi-", "一半，对半"),
        "交" to RadicalEntry("cruzar, entrelazar, amistad", "cross, intersect, mingle", "交会，交集"),
        "占" to RadicalEntry("ocupar, pronosticar, adivinar", "occupy, divine, seize", "占领，占卜"),
        "主" to RadicalEntry("señor, principal, dueño", "master, main, host", "主人，主导"),
        "亡" to RadicalEntry("desaparecer, perecer, huir", "perish, flee, lose", "流亡，逝去"),
        "元" to RadicalEntry("origen, primario, cabeza", "origin, primal, first", "开元，元始"),
        "正" to RadicalEntry("recto, correcto, justo", "straight, correct, upright", "端正，正义"),
        "央" to RadicalEntry("centro, infinito, medio", "center, middle, boundless", "中央，无极"),
        "未" to RadicalEntry("aún no, futuro, octava rama", "not yet, future", "未来，尚未"),
        "末" to RadicalEntry("final, extremo, punta", "end, tip, minor", "末梢，终了"),
        "本" to RadicalEntry("raíz, fundamento, libro", "root, origin, volume", "根基，原本"),
        "友" to RadicalEntry("amigo, fraternidad", "friend, friendship", "友爱，朋友"),
        "爱" to RadicalEntry("amor, afecto, aprecio", "love, affection", "爱慕，关爱"),
        "益" to RadicalEntry("beneficio, provecho, aumento", "benefit, profit, increase", "益处，增进"),
        "委" to RadicalEntry("encomendar, delegar", "entrust, delegate", "委派，委任"),
        "岂" to RadicalEntry("acaso, cómo (retórico)", "how (rhetorical), surely", "岂敢，难道"),
        "白" to RadicalEntry("blanco, pureza, luz", "white, pure, clear", "洁白，明朗"),
        "林" to RadicalEntry("bosque, arboleda", "forest, woods", "丛林，树林"),
        "森" to RadicalEntry("selva densa, frondoso", "dense forest, vast", "森林，茂密")
    )

    fun getRadicalMeaning(symbol: String, defaultMeaning: String, lang: AppLanguage): String {
        val entry = radicalMap[symbol]
        if (entry != null) {
            return when (lang) {
                AppLanguage.SPANISH -> entry.es
                AppLanguage.ENGLISH -> entry.en
                AppLanguage.CHINESE -> entry.zh
            }
        }
        if (defaultMeaning.isBlank()) {
            return symbol
        }

        // Si defaultMeaning está en español proveniente del dataset, traducirlo al idioma activo
        return when (lang) {
            AppLanguage.SPANISH -> defaultMeaning
            AppLanguage.ENGLISH -> translateSpanishGlossToEnglish(defaultMeaning)
            AppLanguage.CHINESE -> translateSpanishGlossToChinese(defaultMeaning)
        }
    }

    private fun translateSpanishGlossToEnglish(text: String): String {
        val lower = text.lowercase()
        return when {
            lower.contains("persona") || lower.contains("humano") -> "person, human quality"
            lower.contains("acción manual") || lower.contains("mano") -> "hand action, manual craft"
            lower.contains("agua") || lower.contains("líquido") -> "water, fluid, liquid flow"
            lower.contains("vegetación") || lower.contains("planta") || lower.contains("hierba") -> "vegetation, plant, herbs"
            lower.contains("árbol") || lower.contains("madera") -> "tree, wood, timber"
            lower.contains("metal") || lower.contains("oro") || lower.contains("hierro") -> "metal, gold, iron tool"
            lower.contains("insecto") || lower.contains("gusano") -> "insect, worm, reptile"
            lower.contains("tierra") || lower.contains("barro") || lower.contains("suelo") -> "earth, soil, ground"
            lower.contains("músculo") || lower.contains("carne") || lower.contains("órgano") -> "flesh, muscle, body organ"
            lower.contains("lenguaje") || lower.contains("habla") || lower.contains("conversación") -> "speech, words, verbal interaction"
            lower.contains("hilo") || lower.contains("seda") || lower.contains("cuerda") -> "silk thread, cord, connection"
            lower.contains("mujer") || lower.contains("femenino") -> "woman, female, maternal"
            lower.contains("corazón") || lower.contains("sentimiento") || lower.contains("emoción") -> "heart, feelings, inner thought"
            lower.contains("colina") || lower.contains("ciudad") || lower.contains("elevación") -> "hill, elevation, ancient town"
            lower.contains("sol") || lower.contains("día") || lower.contains("luz") -> "sun, day, bright daylight"
            lower.contains("bambú") || lower.contains("caña") -> "bamboo, cane utensils"
            lower.contains("roca") || lower.contains("piedra") -> "stone, rock, mineral"
            lower.contains("fuego") || lower.contains("llama") -> "fire, flame, heat"
            lower.contains("montaña") || lower.contains("cumbre") -> "mountain, steep ridge"
            lower.contains("pie") || lower.contains("pierna") || lower.contains("caminar") -> "foot, walk, step"
            lower.contains("enfermedad") || lower.contains("patología") -> "sickness, disease, ache"
            lower.contains("ojo") || lower.contains("mirada") -> "eye, vision, look"
            lower.contains("rey") || lower.contains("jade") -> "king, ruler, precious jade"
            lower.contains("caballo") -> "horse, steed, swift"
            lower.contains("ropa") || lower.contains("vestimenta") || lower.contains("prenda") -> "clothing, garment, attire"
            lower.contains("arroz") || lower.contains("grano") -> "rice, grain, nourishment"
            lower.contains("coche") || lower.contains("carro") || lower.contains("vehículo") -> "vehicle, chariot, carriage"
            lower.contains("puerta") || lower.contains("portal") -> "door, gateway, household"
            lower.contains("techo") || lower.contains("hogar") || lower.contains("casa") -> "roof, shelter, home"
            lower.contains("cuchillo") || lower.contains("corte") -> "knife, blade, cutting division"
            lower.contains("boca") || lower.contains("abertura") -> "mouth, opening, voice"
            else -> formatL10nDefinition(text, AppLanguage.ENGLISH)
        }
    }

    private fun translateSpanishGlossToChinese(text: String): String {
        val lower = text.lowercase()
        return when {
            lower.contains("persona") || lower.contains("humano") -> "人部，立人之势"
            lower.contains("acción manual") || lower.contains("mano") -> "提手旁，手动操作"
            lower.contains("agua") || lower.contains("líquido") -> "三点水，润泽水流"
            lower.contains("vegetación") || lower.contains("planta") || lower.contains("hierba") -> "草字头，植物百草"
            lower.contains("árbol") || lower.contains("madera") -> "木字旁，树木良材"
            lower.contains("metal") || lower.contains("oro") || lower.contains("hierro") -> "金字旁，矿藏利器"
            lower.contains("insecto") || lower.contains("gusano") -> "虫字旁，百虫幼生"
            lower.contains("tierra") || lower.contains("barro") || lower.contains("suelo") -> "土字旁，土地大地"
            lower.contains("músculo") || lower.contains("carne") || lower.contains("órgano") -> "月字旁（肉部），机体气血"
            lower.contains("lenguaje") || lower.contains("habla") || lower.contains("conversación") -> "言字旁，辞令交谈"
            lower.contains("hilo") || lower.contains("seda") || lower.contains("cuerda") -> "绞丝旁，经纶丝缕"
            lower.contains("mujer") || lower.contains("femenino") -> "女字旁，温婉母性"
            lower.contains("corazón") || lower.contains("sentimiento") || lower.contains("emoción") -> "竖心旁，内省情感"
            lower.contains("colina") || lower.contains("ciudad") || lower.contains("elevación") -> "双耳旁，山势原邑"
            lower.contains("sol") || lower.contains("día") || lower.contains("luz") -> "日字旁，天日光芒"
            lower.contains("bambú") || lower.contains("caña") -> "竹字头，节气器具"
            lower.contains("roca") || lower.contains("piedra") -> "石字旁，崖壁坚石"
            lower.contains("fuego") || lower.contains("llama") -> "火字旁，薪柴炽热"
            lower.contains("montaña") || lower.contains("cumbre") -> "山峰叠嶂"
            lower.contains("pie") || lower.contains("pierna") || lower.contains("caminar") -> "足部行止"
            lower.contains("enfermedad") || lower.contains("patología") -> "病字旁，卧榻养息"
            lower.contains("ojo") || lower.contains("mirada") -> "目字旁，洞察注视"
            lower.contains("rey") || lower.contains("jade") -> "王字旁，美玉温润"
            lower.contains("caballo") -> "马部，纵马驰骋"
            lower.contains("ropa") || lower.contains("vestimenta") || lower.contains("prenda") -> "衣字旁，锦缎遮体"
            lower.contains("arroz") || lower.contains("grano") -> "米字旁，岁稔年丰"
            lower.contains("coche") || lower.contains("carro") || lower.contains("vehículo") -> "车字旁，舟车辐辏"
            lower.contains("puerta") || lower.contains("portal") -> "门字框，门户迎送"
            lower.contains("techo") || lower.contains("hogar") || lower.contains("casa") -> "宝盖头，避雨庇护"
            lower.contains("cuchillo") || lower.contains("corte") -> "立刀旁，削割辨物"
            lower.contains("boca") || lower.contains("abertura") -> "口部，吞吐发声"
            else -> formatL10nDefinition(text, AppLanguage.CHINESE)
        }
    }

    // Traducción de etiquetas sintácticas de CEDICT
    private val tagMapEs = mapOf(
        "(literary)" to "(literario)",
        "(bound form)" to "(forma ligada)",
        "(classical)" to "(clásico)",
        "(coll.)" to "(coloquial)",
        "(dialect)" to "(dialectal)",
        "(slang)" to "(jerga)",
        "(math.)" to "(matemáticas)",
        "(chemistry)" to "(química)",
        "(physics)" to "(física)",
        "(archaic)" to "(arcaico)",
        "(idiom)" to "(modismo)",
        "(buddhism)" to "(budismo)",
        "(tcm)" to "(medicina tradicional china)",
        "(used in transliterating chemical names)" to "(términos químicos)",
        "(used in transliteration)" to "(transliteración)",
        "(loanword)" to "(préstamo léxico)",
        "(fig.)" to "(figurativo)",
        "(esp.)" to "(especialmente)",
        "(Tw)" to "(Taiwán)",
        "(HK)" to "(Hong Kong)",
        "(Cantonese)" to "(cantonés)",
        "(onom.)" to "(onomatopeya)",
        "(polite)" to "(cortés)",
        "(derog.)" to "(despectivo)",
        "(humble)" to "(humilde)",
        "(interj.)" to "(interjección)"
    )

    private val tagMapZh = mapOf(
        "(literary)" to "(文言)",
        "(bound form)" to "(构词语素)",
        "(classical)" to "(古语)",
        "(coll.)" to "(口语)",
        "(dialect)" to "(方言)",
        "(slang)" to "(俚语)",
        "(math.)" to "(数学)",
        "(chemistry)" to "(化学)",
        "(physics)" to "(物理)",
        "(archaic)" to "(古通)",
        "(idiom)" to "(成语)",
        "(buddhism)" to "(佛教)",
        "(tcm)" to "(中医)",
        "(used in transliterating chemical names)" to "(化学音译)",
        "(used in transliteration)" to "(音译专用)",
        "(loanword)" to "(外来借词)",
        "(fig.)" to "(比喻)",
        "(esp.)" to "(尤指)",
        "(Tw)" to "(台湾)",
        "(HK)" to "(香港)",
        "(Cantonese)" to "(粤语)",
        "(onom.)" to "(拟声)",
        "(polite)" to "(敬语)",
        "(derog.)" to "(贬义)",
        "(humble)" to "(谦辞)",
        "(interj.)" to "(叹词)"
    )

    // Glosario canónico integral de equivalencias CEDICT -> (Español, Chino)
    private val glossaryDirect: Map<String, Pair<String, String>> = mapOf(
        "a bit" to Pair("un poco", "一点"),
        "a small amount" to Pair("pequeña cantidad", "微量，少量"),
        "abbreviation" to Pair("abreviatura", "简称，缩写"),
        "about" to Pair("aproximadamente; sobre", "大约；关于"),
        "abundant" to Pair("abundante, copioso", "丰富，充裕"),
        "accept" to Pair("aceptar, admitir", "接受，收受"),
        "adjacent to" to Pair("adyacente a", "相邻"),
        "affect" to Pair("afectar, influir", "影响，波及"),
        "afternoon" to Pair("tarde (después de mediodía)", "下午"),
        "again" to Pair("de nuevo, otra vez", "再，又"),
        "agree" to Pair("estar de acuerdo", "同意，赞同"),
        "air" to Pair("aire, atmósfera", "空气"),
        "airplane" to Pair("avión, aeronave", "飞机"),
        "airport" to Pair("aeropuerto", "机场，飞机场"),
        "all" to Pair("todo, todos", "全部，所有"),
        "almost" to Pair("casi, por poco", "几乎，差不多"),
        "already" to Pair("ya", "已经"),
        "also" to Pair("también, asimismo", "也，并且"),
        "although" to Pair("aunque, a pesar de que", "虽然，尽管"),
        "always" to Pair("siempre, constantemente", "总是，一直"),
        "america" to Pair("Estados Unidos, América", "美国，美洲"),
        "and" to Pair("y, e; con", "和，与，跟"),
        "angry" to Pair("enojado, furioso", "生气，愤怒"),
        "anhui" to Pair("Anhui (provincia china)", "安徽省"),
        "animal" to Pair("animal", "动物"),
        "answer" to Pair("respuesta, solución", "答案，回答"),
        "any" to Pair("cualquier, alguno", "任何"),
        "appear" to Pair("aparecer, figurar", "出现，呈现"),
        "apple" to Pair("manzana", "苹果"),
        "arise" to Pair("surgir, brotar", "产生，升起"),
        "arm" to Pair("brazo", "手臂，胳膊"),
        "arrive" to Pair("llegar, arribar", "到达，到"),
        "ask" to Pair("preguntar; pedir", "问，询问；请求"),
        "aspect particle" to Pair("partícula aspectual", "动态助词"),
        "assist" to Pair("asistir, auxiliar", "协助，支援"),
        "attend" to Pair("asistir a, cursar", "就读，参加"),
        "aunt" to Pair("tía", "阿姨，姑姑，婶婶"),
        "australia" to Pair("Australia", "澳大利亚"),
        "autumn" to Pair("otoño", "秋天，秋季"),
        "baby" to Pair("bebé, infante", "婴儿，宝宝"),
        "back" to Pair("detrás, espalda", "后，后面"),
        "bad" to Pair("malo, pernicioso", "坏，恶劣的"),
        "bag" to Pair("bolso, cartera, saco", "包，袋子"),
        "banana" to Pair("plátano, banana", "香蕉"),
        "bank" to Pair("banco, entidad financiera", "银行"),
        "bathroom" to Pair("baño, sanitario", "卫生间，洗手间"),
        "be fond of" to Pair("gustar, tener cariño a", "喜爱"),
        "beautiful" to Pair("hermoso, bello", "美丽，漂亮"),
        "because" to Pair("porque, dado que", "因为，由于"),
        "become" to Pair("convertirse en, volverse", "成为，变成"),
        "bed" to Pair("cama", "床"),
        "beef" to Pair("carne de res, ternera", "牛肉"),
        "beer" to Pair("cerveza", "啤酒"),
        "begin" to Pair("iniciar, comenzar", "开始"),
        "beijing" to Pair("Pekín, Beijing", "北京"),
        "believe" to Pair("creer, confiar", "相信，信任"),
        "bicycle" to Pair("bicicleta", "自行车"),
        "big" to Pair("grande, vasto", "大，巨大的"),
        "bike" to Pair("bici, bicicleta", "自行车，单车"),
        "bird" to Pair("pájaro, ave", "鸟"),
        "bitter" to Pair("amargo, áspero", "苦"),
        "boat" to Pair("barco, barca", "船"),
        "body" to Pair("cuerpo, organismo", "身体"),
        "book" to Pair("libro, tomo", "书，书籍"),
        "borrow" to Pair("pedir prestado", "借，借入"),
        "boss" to Pair("jefe, patrón", "老板，上司"),
        "both" to Pair("ambos, los dos", "两者，皆"),
        "boy" to Pair("niño, muchacho", "男孩"),
        "brave" to Pair("valiente, audaz", "勇敢"),
        "bread" to Pair("pan", "面包"),
        "break" to Pair("romper, quebrantar", "破，折断"),
        "bright" to Pair("brillante, luminoso", "明亮，光亮"),
        "bring" to Pair("traer, acarrear", "带，带来"),
        "britain" to Pair("Gran Bretaña", "英国"),
        "brother" to Pair("hermano", "兄弟"),
        "build" to Pair("construir, edificar", "建造，建筑"),
        "building" to Pair("edificio, inmueble", "大楼，建筑"),
        "bus" to Pair("autobús, bus", "公交车，公共汽车"),
        "busy" to Pair("ocupado, atareado", "忙，繁忙"),
        "but" to Pair("pero, sin embargo", "但是，可是"),
        "buy" to Pair("comprar, adquirir", "买，购买"),
        "call" to Pair("llamar, telefonear", "叫，呼叫，打电话"),
        "canada" to Pair("Canadá", "加拿大"),
        "capital" to Pair("capital, fondo", "资本，本钱"),
        "car" to Pair("coche, carro, automóvil", "汽车，车"),
        "careful" to Pair("cuidadoso, atento", "小心，仔细"),
        "carry" to Pair("cargar, portar", "携带，挑"),
        "cat" to Pair("gato, felino", "猫"),
        "catch" to Pair("atrapar, capturar", "抓，抓获"),
        "cause" to Pair("causar, originar", "引起，造成"),
        "celebrate" to Pair("celebrar, festejar", "庆祝，欢庆"),
        "certainly" to Pair("ciertamente, sin duda", "一定，必定"),
        "chair" to Pair("silla", "椅子"),
        "change" to Pair("cambiar, transformar", "变，改变"),
        "character" to Pair("carácter, signo", "字，汉字"),
        "cheap" to Pair("barato, económico", "便宜，廉价"),
        "check" to Pair("comprobar, revisar", "检查，核对"),
        "chicken" to Pair("pollo, gallina", "鸡"),
        "child" to Pair("niño, hijo", "孩子，儿童"),
        "children" to Pair("niños, hijos", "孩子们"),
        "china" to Pair("China", "中国"),
        "chinese language" to Pair("idioma chino, mandarín", "汉语，中文"),
        "choose" to Pair("elegir, seleccionar", "选，挑选"),
        "cinema" to Pair("cine, sala cinematográfica", "电影院"),
        "city" to Pair("ciudad, urbe", "城市，都市"),
        "classifier" to Pair("clasificador", "量词"),
        "classmate" to Pair("compañero de clase", "同学，同窗"),
        "classroom" to Pair("aula, salón de clase", "教室"),
        "clean" to Pair("limpio, aseado", "干净，清洁"),
        "clear" to Pair("claro, nítido, transparente", "清晰，明朗，清澈"),
        "clever" to Pair("listo, ingenioso", "灵巧，机敏"),
        "clock" to Pair("reloj de pared", "钟，时钟"),
        "close" to Pair("cerrar, clausurar", "关，关闭"),
        "close to" to Pair("cercano a", "紧邻，靠拢"),
        "clothes" to Pair("ropa, vestimenta", "衣服"),
        "cloud" to Pair("nube", "云，云彩"),
        "coffee" to Pair("café", "咖啡"),
        "cold" to Pair("frío, gélido", "冷，寒冷"),
        "colleague" to Pair("colega, compañero de trabajo", "同事"),
        "collect" to Pair("recoger, coleccionar", "收集，收藏"),
        "come" to Pair("venir, llegar", "来，到来"),
        "come out" to Pair("salir, emerger", "出来，现出"),
        "comfortable" to Pair("cómodo, confortable", "舒服，舒适"),
        "computer" to Pair("ordenador, computadora", "电脑，计算机"),
        "congratulate" to Pair("felicitar", "祝贺，恭喜"),
        "consume" to Pair("consumir, agotar", "消耗，食用"),
        "continue" to Pair("continuar, proseguir", "继续，持续"),
        "convenient" to Pair("conveniente, práctico", "方便"),
        "cook" to Pair("cocinar, preparar comida", "做饭，烹饪"),
        "cool" to Pair("fresco, templado", "凉，凉爽"),
        "correct" to Pair("correcto, acertado", "正确，对"),
        "cost" to Pair("costar, valer", "花费，值"),
        "country" to Pair("país, nación", "国家，国度"),
        "cousin" to Pair("primo, prima", "表兄妹，堂兄妹"),
        "cover" to Pair("cubrir, tapar", "遮盖，覆盖"),
        "cow" to Pair("vaca, res", "牛"),
        "cry" to Pair("llorar, lamentar", "哭，流泪"),
        "cup" to Pair("taza, copa", "杯子，茶杯"),
        "cure" to Pair("curar, sanar", "治愈，治疗"),
        "customer" to Pair("cliente, comprador", "顾客，客户"),
        "cut" to Pair("cortar, rebanar", "切，剪，割"),
        "dad" to Pair("papá", "爸爸"),
        "dance" to Pair("bailar, danzar", "跳舞，跳"),
        "dangerous" to Pair("peligroso, arriesgado", "危险"),
        "dark" to Pair("oscuro, sombrío", "黑暗，昏暗"),
        "daughter" to Pair("hija", "女儿"),
        "day" to Pair("día, jornada", "天，日"),
        "decide" to Pair("decidir, determinar", "决定"),
        "decrease" to Pair("disminuir, reducir", "减少，缩减"),
        "defeat" to Pair("derrotar, abatir", "击败，打败"),
        "delicious" to Pair("delicioso, sabroso", "好吃，美味"),
        "desire" to Pair("desear, anhelar", "愿望，渴望"),
        "destroy" to Pair("destruir, arruinar", "破坏，毁灭"),
        "develop" to Pair("desarrollar, evolucionar", "发展，开辟"),
        "die" to Pair("morir, perecer", "死亡，死去"),
        "different" to Pair("diferente, distinto", "不同，各异"),
        "difficult" to Pair("difícil, arduo", "难，困难"),
        "dirty" to Pair("sucio, manchado", "脏，污秽"),
        "dish" to Pair("plato, vianda", "菜肴，菜盘"),
        "do" to Pair("hacer, realizar", "做，干"),
        "doctor" to Pair("médico, doctor", "医生，大夫"),
        "dog" to Pair("perro, can", "狗"),
        "dollar" to Pair("dólar", "美元"),
        "door" to Pair("puerta, portal", "门，大门"),
        "down" to Pair("abajo, hacia abajo", "下，向下"),
        "drink" to Pair("beber, tomar", "喝，饮用"),
        "drive" to Pair("conducir, manejar", "开，驾驶"),
        "driver" to Pair("conductor, chófer", "司机，驾驶员"),
        "drop" to Pair("soltar, caer", "掉，落下"),
        "each" to Pair("cada uno", "各个，各"),
        "ear" to Pair("oreja, oído", "耳朵"),
        "ears" to Pair("orejas, oídos", "耳朵"),
        "earth" to Pair("tierra, planeta Tierra", "地球，大地"),
        "east" to Pair("este, oriente", "东，东方"),
        "easy" to Pair("fácil, sencillo", "容易，简便"),
        "eat" to Pair("comer, alimentarse", "吃，进食"),
        "egg" to Pair("huevo", "鸡蛋，蛋"),
        "eight" to Pair("ocho", "八"),
        "elder brother" to Pair("hermano mayor", "哥哥"),
        "elder sister" to Pair("hermana mayor", "姐姐"),
        "electric" to Pair("eléctrico", "电的，电动"),
        "electricity" to Pair("electricidad, fluido", "电，电力"),
        "emperor" to Pair("emperador", "皇帝"),
        "empty" to Pair("vacío, desocupado", "空，空着"),
        "end" to Pair("terminar, acabar", "终了，结束"),
        "england" to Pair("Inglaterra", "英国，英格兰"),
        "english language" to Pair("idioma inglés", "英语，英文"),
        "eradicate" to Pair("erradicar, extirpar", "根除，消灭"),
        "even" to Pair("incluso, aun", "甚至，连"),
        "evening" to Pair("tarde-noche, anochecer", "晚上，傍晚"),
        "every" to Pair("cada, todo", "每个，凡"),
        "everyone" to Pair("todos, cada uno", "大家，所有人"),
        "everywhere" to Pair("en todas partes", "到处，处处"),
        "exceed" to Pair("superar, exceder", "超越，超出"),
        "excellent" to Pair("excelente, óptimo", "优秀，卓越"),
        "exercise" to Pair("hacer ejercicio", "锻炼，运动"),
        "expensive" to Pair("caro, costoso", "贵，昂贵"),
        "eye" to Pair("ojo", "眼睛"),
        "eyes" to Pair("ojos", "眼睛"),
        "face" to Pair("rostro, cara", "脸，面部"),
        "fail" to Pair("reprobar, fracasar", "不及格，失败"),
        "fall" to Pair("otoño; caída", "秋天；跌落"),
        "false" to Pair("falso, inexacto", "假，虚假"),
        "famous" to Pair("famoso, célebre", "著名，有名"),
        "far" to Pair("lejos, distante", "远，遥远"),
        "fast" to Pair("rápido, veloz", "快，迅速"),
        "father" to Pair("padre, papá", "父亲，爸爸"),
        "fear" to Pair("temer, tener miedo", "害怕，畏惧"),
        "feel" to Pair("sentir, percibir", "感觉，体会"),
        "feet" to Pair("pies", "双脚"),
        "few" to Pair("pocos, escasos", "很少，少数"),
        "fight" to Pair("luchar, combatir", "战斗，搏斗"),
        "film" to Pair("película", "电影，影片"),
        "find" to Pair("encontrar, hallar", "找，发现"),
        "finish" to Pair("terminar, finalizar", "结束，完成"),
        "fire" to Pair("fuego, llama", "火"),
        "first" to Pair("primero", "第一"),
        "fish" to Pair("pescado, pez", "鱼"),
        "five" to Pair("cinco", "五"),
        "flower" to Pair("flor", "花，花朵"),
        "fly" to Pair("volar, pilotar", "飞，飞翔"),
        "follow" to Pair("seguir, obedecer", "跟随，遵循"),
        "food" to Pair("comida, alimento", "食物，佳肴"),
        "foot" to Pair("pie; pata", "脚"),
        "forget" to Pair("olvidar, desatender", "忘记，忘"),
        "forgive" to Pair("perdonar, disculpar", "原谅，宽恕"),
        "four" to Pair("cuatro", "四"),
        "france" to Pair("Francia", "法国"),
        "free" to Pair("libre; gratuito", "空闲；免费"),
        "fresh" to Pair("fresco, novedoso", "新鲜"),
        "friend" to Pair("amigo, compadre", "朋友，友人"),
        "friendly" to Pair("amistoso, afable", "友好，友善"),
        "friends" to Pair("amigos", "朋友们"),
        "front" to Pair("frente, delante", "前，前面"),
        "fruit" to Pair("fruta", "水果"),
        "fujian" to Pair("Fujian (provincia china)", "福建省"),
        "full" to Pair("lleno, repleto; saciado", "满；饱"),
        "gansu" to Pair("Gansu (provincia china)", "甘肃省"),
        "garden" to Pair("jardín, huerto", "花园，庭院"),
        "germany" to Pair("Alemania", "德国"),
        "get" to Pair("conseguir, obtener", "得到，获取"),
        "get up" to Pair("levantarse de la cama", "起床"),
        "girl" to Pair("niña, muchacha", "女孩"),
        "give" to Pair("dar, entregar", "给，赠予"),
        "glass" to Pair("vaso, cristal", "玻璃杯，玻璃"),
        "go" to Pair("ir, marchar", "去，走"),
        "go out" to Pair("salir, marchar", "出去，外出"),
        "gold" to Pair("oro, metal áureo", "黄金"),
        "good" to Pair("bueno, bondadoso", "好，优良的"),
        "grand" to Pair("grandioso, majestuoso", "宏伟，壮丽"),
        "grandfather" to Pair("abuelo", "祖父，爷爷，外公"),
        "grandma" to Pair("abuelita", "奶奶，外婆"),
        "grandmother" to Pair("abuela", "祖母，奶奶，外婆"),
        "grandpa" to Pair("abuelito", "爷爷，外公"),
        "grandparents" to Pair("abuelos", "祖父母，外祖父母"),
        "grass" to Pair("hierba, pasto", "草，草坪"),
        "great" to Pair("genial, magnífico", "太棒了，伟大"),
        "greens" to Pair("verduras, hojas verdes", "青菜"),
        "grief" to Pair("duelo, congoja", "哀痛，悲戚"),
        "grow" to Pair("crecer, cultivar", "生长，种植"),
        "guangdong" to Pair("Guangdong (provincia china)", "广东省"),
        "guangxi" to Pair("Guangxi (región autónoma)", "广西"),
        "guest" to Pair("invitado, huésped", "客人，宾客"),
        "guizhou" to Pair("Guizhou (provincia china)", "贵州省"),
        "hainan" to Pair("Hainan (provincia china)", "海南省"),
        "hair" to Pair("cabello, pelo", "头发，毛发"),
        "half" to Pair("mitad, medio", "一半，半"),
        "hand" to Pair("mano", "手"),
        "hands" to Pair("manos", "双手"),
        "happy" to Pair("feliz, alegre", "快乐，幸福，高兴"),
        "hard" to Pair("duro; difícil", "硬；难"),
        "harm" to Pair("dañar, perjudicar", "伤害，危害"),
        "hat" to Pair("sombrero, gorro", "帽子"),
        "hate" to Pair("odiar, detestar", "讨厌，恨"),
        "he" to Pair("él", "他"),
        "head" to Pair("cabeza", "头"),
        "healthy" to Pair("saludable, sano", "健康"),
        "hear" to Pair("oír, escuchar", "听见，闻"),
        "heart" to Pair("corazón", "心，心脏"),
        "heavy" to Pair("pesado, denso", "重，沉重"),
        "hebei" to Pair("Hebei (provincia china)", "河北省"),
        "heilongjiang" to Pair("Heilongjiang (provincia china)", "黑龙江省"),
        "help" to Pair("ayudar, auxiliar", "帮，帮助"),
        "henan" to Pair("Henan (provincia china)", "河南省"),
        "her" to Pair("ella, la", "她"),
        "here" to Pair("aquí, acá", "这里，这儿"),
        "hide" to Pair("ocultar, esconder", "藏，躲藏"),
        "high" to Pair("alto, supremo", "高耸，崇高"),
        "him" to Pair("él, le", "他"),
        "hit" to Pair("golpear, asestar", "打，击"),
        "hold" to Pair("sostener, agarrar", "持，拿住"),
        "home" to Pair("hogar, casa", "家，家园"),
        "honest" to Pair("honesto, honrado", "诚实，正直"),
        "hong kong" to Pair("Hong Kong", "香港"),
        "hope" to Pair("esperar, anhelar", "希望"),
        "horse" to Pair("caballo", "马"),
        "hospital" to Pair("hospital, clínica", "医院"),
        "host" to Pair("anfitrión, dueño", "主人，东道主"),
        "hot" to Pair("caliente, caluroso", "热，炎热"),
        "hotel" to Pair("hotel, hostal", "酒店，饭店，宾馆"),
        "hour" to Pair("hora", "小时，钟头"),
        "house" to Pair("casa, vivienda", "房屋，房子"),
        "how" to Pair("cómo, de qué manera", "怎么，如何"),
        "how many" to Pair("cuántos", "多少，几个"),
        "how much" to Pair("cuánto", "多少"),
        "hubei" to Pair("Hubei (provincia china)", "湖北省"),
        "human" to Pair("humano, individuo", "人类"),
        "hunan" to Pair("Hunan (provincia china)", "湖南省"),
        "hundred" to Pair("cien, ciento", "百"),
        "hungry" to Pair("hambriento, con hambre", "饿，饥饿"),
        "hurry" to Pair("darse prisa", "赶快，急忙"),
        "husband" to Pair("esposo, marido", "丈夫，老公"),
        "i" to Pair("yo", "我"),
        "if" to Pair("si (condicional)", "如果，要是"),
        "ignite" to Pair("encender fuego", "点火，引燃"),
        "ill" to Pair("enfermo", "有病，患病"),
        "important" to Pair("importante, fundamental", "重要，要紧"),
        "improve" to Pair("mejorar, perfeccionar", "提高，改进"),
        "in" to Pair("dentro, en", "里，在……里"),
        "in order" to Pair("en orden", "有序，依序"),
        "in sequence" to Pair("en secuencia", "顺次，循序"),
        "increase" to Pair("aumentar, incrementar", "增加，增长"),
        "india" to Pair("India", "印度"),
        "inn" to Pair("posada, hostal", "客栈，旅店"),
        "inner mongolia" to Pair("Mongolia Interior", "内蒙古"),
        "interjection" to Pair("interjección", "感叹词，叹词"),
        "introduce" to Pair("presentar, introducir", "介绍"),
        "invite" to Pair("invitar, convidar", "邀请，请"),
        "it" to Pair("ello, eso", "它"),
        "italy" to Pair("Italia", "意大利"),
        "japan" to Pair("Japón", "日本"),
        "jiangsu" to Pair("Jiangsu (provincia china)", "江苏省"),
        "jiangxi" to Pair("Jiangxi (provincia china)", "江西省"),
        "jilin" to Pair("Jilin (provincia china)", "吉林省"),
        "juice" to Pair("zumo, jugo", "果汁"),
        "just" to Pair("justo, apenas; solo", "刚，刚刚；只"),
        "kill" to Pair("matar, ultimar", "杀，杀死"),
        "king" to Pair("rey, monarca", "国王，君主"),
        "kitchen" to Pair("cocina", "厨房"),
        "know" to Pair("saber, conocer", "知道，认识"),
        "korea" to Pair("Corea", "韩国，朝鲜"),
        "lake" to Pair("lago", "湖，湖泊"),
        "language" to Pair("idioma, lengua", "语言"),
        "large" to Pair("grande, amplio", "大，广阔的"),
        "laugh" to Pair("reírse, carcajearse", "笑，大笑"),
        "lead" to Pair("guiar, capitanear", "带领，领导"),
        "leader" to Pair("líder, dirigente", "领导，领袖"),
        "learn" to Pair("aprender, instruirse", "学，学习"),
        "leave" to Pair("partir, salir, marcharse", "离开，出发"),
        "left" to Pair("izquierda", "左，左边"),
        "leg" to Pair("pierna", "腿"),
        "lend" to Pair("prestar, prestar a", "借出"),
        "letter" to Pair("carta, misiva; letra", "信，书信；字母"),
        "liaoning" to Pair("Liaoning (provincia china)", "辽宁省"),
        "library" to Pair("biblioteca", "图书馆"),
        "lie" to Pair("yacer, tumbarse; mentir", "躺，卧；说谎"),
        "lift" to Pair("levantar, alzar", "举，抬"),
        "light" to Pair("ligero, liviano; luz", "轻；光"),
        "lightning" to Pair("relámpago, rayo", "闪电"),
        "like" to Pair("gustar, agradar", "喜欢，喜爱"),
        "listen" to Pair("escuchar, oír", "听，倾听"),
        "little" to Pair("poco, pequeño", "少许，微小"),
        "live" to Pair("vivir, residir", "住，居住，生活"),
        "look" to Pair("mirar, observar", "看，瞧"),
        "look at" to Pair("mirar a", "注视，观看"),
        "look for" to Pair("buscar, indagar", "寻找，找"),
        "lose" to Pair("perder, extraviar", "丢失，失去"),
        "lose (game)" to Pair("perder", "输"),
        "love" to Pair("amar, querer", "爱，热爱"),
        "low" to Pair("bajo, humilde", "低下，微薄"),
        "machine" to Pair("máquina, motor", "机器"),
        "make" to Pair("hacer, fabricar", "做，制作"),
        "malaysia" to Pair("Malasia", "马来西亚"),
        "man" to Pair("hombre, varón", "男人"),
        "manage" to Pair("gestionar, administrar", "管理，经营"),
        "many" to Pair("muchos, abundante", "许多，多"),
        "marinate" to Pair("marinar", "腌渍"),
        "market" to Pair("mercado, plaza de abastos", "市场，集市"),
        "master" to Pair("maestro, perito", "大师，宗师"),
        "maybe" to Pair("tal vez, quizás", "也许，可能"),
        "me" to Pair("mí, me", "我"),
        "measure word" to Pair("palabra de medida, clasificador", "量词"),
        "meat" to Pair("carne", "肉"),
        "meet" to Pair("reunirse, encontrarse", "见，遇见，会面"),
        "men" to Pair("hombres", "男人"),
        "message" to Pair("mensaje, recado", "信息，消息"),
        "metal" to Pair("metal", "金属"),
        "middle" to Pair("medio, centro", "中间，当中"),
        "milk" to Pair("leche", "牛奶"),
        "minute" to Pair("minuto", "分钟"),
        "modal particle" to Pair("partícula modal", "语气助词"),
        "mom" to Pair("mamá", "妈妈"),
        "money" to Pair("dinero, plata", "钱，货币"),
        "month" to Pair("mes", "月，月份"),
        "moon" to Pair("luna", "月亮"),
        "more" to Pair("más, mayor", "更多，较多"),
        "morning" to Pair("mañana, madrugada", "早上，早晨"),
        "most" to Pair("la mayoría, el más", "最多，绝大多数"),
        "mother" to Pair("madre, mamá", "母亲，妈妈"),
        "mountain" to Pair("montaña, monte", "山，山峰"),
        "mouth" to Pair("boca", "嘴，口"),
        "move" to Pair("mover, trasladar", "移动，搬迁"),
        "movie" to Pair("película, filme", "电影"),
        "much" to Pair("mucho", "许多"),
        "mum" to Pair("mamá", "妈妈"),
        "museum" to Pair("museo", "博物馆"),
        "music" to Pair("música", "音乐"),
        "name" to Pair("nombre, apelativo", "名字，姓名"),
        "nation" to Pair("nación, pueblo", "民族，国家"),
        "near" to Pair("cerca, próximo", "近，靠近"),
        "need" to Pair("necesitar, requerir", "需要"),
        "neighbor" to Pair("vecino", "邻居"),
        "never" to Pair("nunca, jamás", "从不，绝不"),
        "new" to Pair("nuevo, reciente", "新，崭新"),
        "news" to Pair("noticia, novedades", "新闻，消息"),
        "night" to Pair("noche", "夜晚，晚上"),
        "nine" to Pair("nueve", "九"),
        "ningxia" to Pair("Ningxia (región autónoma)", "宁夏"),
        "no" to Pair("no, ninguno", "不，没有"),
        "nobody" to Pair("nadie", "无人，没有人"),
        "noisy" to Pair("ruidoso, bullicioso", "吵闹，喧闹"),
        "non-" to Pair("no, des-", "非"),
        "none" to Pair("ninguno", "无，没有"),
        "noodles" to Pair("fideos, tallarines", "面条"),
        "noon" to Pair("mediodía", "中午"),
        "normal" to Pair("normal, estándar", "正常"),
        "north" to Pair("norte", "北，北方"),
        "north korea" to Pair("Corea del Norte", "朝鲜"),
        "nose" to Pair("nariz", "鼻子"),
        "not" to Pair("no", "不，非"),
        "not so" to Pair("no tan, no así", "不然，非此"),
        "now" to Pair("ahora, en este momento", "现在，如今"),
        "occur" to Pair("ocurrir, suceder", "发生"),
        "ocean" to Pair("océano", "海洋"),
        "of course" to Pair("por supuesto, desde luego", "当然"),
        "office" to Pair("oficina, despacho", "办公室"),
        "often" to Pair("a menudo, con frecuencia", "经常，常常"),
        "old" to Pair("viejo, antiguo", "旧，老"),
        "on" to Pair("sobre, encima de", "在……上"),
        "one" to Pair("uno", "一"),
        "oneself" to Pair("uno mismo", "自身"),
        "only" to Pair("solo, solamente", "只，仅仅"),
        "open" to Pair("abrir, inaugurar", "开，打开"),
        "or" to Pair("o, u", "或者，还是"),
        "orange" to Pair("naranja", "橙子，橘子"),
        "order" to Pair("ordenar, pedir", "点选，订购"),
        "ordinary" to Pair("ordinario, común", "普通，平常"),
        "origin" to Pair("origen, manantial", "起源，源头"),
        "out" to Pair("fuera, afuera", "外，在外"),
        "pa" to Pair("papá", "爸"),
        "papa" to Pair("papá", "爸爸"),
        "paper" to Pair("papel", "纸，纸张"),
        "parents" to Pair("padres, progenitores", "父母，双亲"),
        "park" to Pair("parque, jardín público", "公园"),
        "particle" to Pair("partícula gramatical", "助词"),
        "pass" to Pair("pasar, aprobar", "过，通过"),
        "patient" to Pair("paciente, enfermo", "病人，患者"),
        "pay" to Pair("pagar, abonar", "付，支付"),
        "peaceful" to Pair("pacífico, sereno", "和平，平静"),
        "pen" to Pair("bolígrafo, pluma", "笔，钢笔"),
        "pencil" to Pair("lápiz", "铅笔"),
        "people" to Pair("personas, gente", "人们，群众"),
        "person" to Pair("persona, ser humano", "人，人类"),
        "phone" to Pair("teléfono, celular", "电话，手机"),
        "phone call" to Pair("llamada telefónica", "电话"),
        "photo" to Pair("fotografía", "照片"),
        "pickle" to Pair("encurtir", "腌菜，泡菜"),
        "picture" to Pair("imagen, foto, cuadro", "照片，图片"),
        "pig" to Pair("cerdo, puerco", "猪"),
        "pity" to Pair("piedad, compasión", "怜悯，可怜"),
        "place" to Pair("lugar, sitio", "地方，处所"),
        "plan" to Pair("planificar, planear", "计划，打算"),
        "plane" to Pair("avión", "飞机"),
        "play" to Pair("jugar; tocar instrumento", "玩，游戏；弹奏"),
        "point" to Pair("señalar, apuntar", "指，指示"),
        "police" to Pair("policía", "警察，公安"),
        "polite" to Pair("educado, cortés", "礼貌，客气"),
        "poor" to Pair("pobre, humilde", "穷，贫困"),
        "pork" to Pair("carne de cerdo", "猪肉"),
        "prefix" to Pair("prefijo", "前缀"),
        "prepare" to Pair("preparar, disponer", "准备"),
        "principal" to Pair("principal, haber", "本金，要务"),
        "probably" to Pair("probablemente", "大概，可能"),
        "produce" to Pair("producir, fabricar", "生产，制造"),
        "pronounce" to Pair("pronunciar", "发音"),
        "protect" to Pair("proteger, resguardar", "保护"),
        "pull" to Pair("tirar, jalar", "拉"),
        "pure" to Pair("puro, genuino", "纯，纯正"),
        "push" to Pair("empujar", "推"),
        "put" to Pair("poner, colocar", "放置，摆放"),
        "put on" to Pair("ponerse (ropa)", "穿上，戴上"),
        "qinghai" to Pair("Qinghai (provincia china)", "青海省"),
        "question" to Pair("pregunta, duda; problema", "问题"),
        "quick" to Pair("rápido, pronto", "迅速的"),
        "quiet" to Pair("tranquilo, silencioso", "安静，幽静"),
        "quite" to Pair("bastante, bien", "挺，相当"),
        "radical" to Pair("radical Kangxi", "部首，偏旁"),
        "rain" to Pair("lluvia", "雨"),
        "raise" to Pair("levantar, alzar", "举起，抬起"),
        "read" to Pair("leer, estudiar", "读，阅读"),
        "read aloud" to Pair("leer en voz alta", "宣读"),
        "read out" to Pair("leer en voz alta", "朗读"),
        "really" to Pair("realmente, de verdad", "真，确实"),
        "receive" to Pair("recibir, acoger", "收到，收下"),
        "refuse" to Pair("rechazar, rehusar", "拒绝"),
        "remember" to Pair("recordar, memorizar", "记得，记起"),
        "repair" to Pair("reparar, arreglar", "修理，修补"),
        "rest" to Pair("descansar, reposar", "休息，歇息"),
        "restaurant" to Pair("restaurante, comedor", "饭馆，餐厅"),
        "return" to Pair("volver, regresar; devolver", "回，归还"),
        "rice" to Pair("arroz, comida", "米饭，米"),
        "rich" to Pair("rico, adinerado", "富，富裕"),
        "ride" to Pair("montar, cabalgar", "骑，乘"),
        "right" to Pair("derecha; correcto", "右，右边；正确"),
        "river" to Pair("río, corriente", "河，江河"),
        "road" to Pair("camino, carretera", "路，道路"),
        "room" to Pair("habitación, cuarto", "房间，屋子"),
        "root" to Pair("raíz, base", "根，根基"),
        "run" to Pair("correr, trotar", "跑，跑步"),
        "russia" to Pair("Rusia", "俄罗斯"),
        "sad" to Pair("triste, apenado", "难过，伤心"),
        "safe" to Pair("seguro, a salvo", "安全"),
        "salt" to Pair("sal", "盐"),
        "salty" to Pair("salado", "咸"),
        "same" to Pair("mismo, idéntico", "同，一样，相同"),
        "save" to Pair("salvar; ahorrar", "救；省，存"),
        "say" to Pair("decir, expresar", "说，道"),
        "school" to Pair("escuela, colegio", "学校，学堂"),
        "sea" to Pair("mar, océano", "海，大海"),
        "season" to Pair("estación del año", "季节"),
        "second" to Pair("segundo (tiempo)", "秒"),
        "see" to Pair("ver, mirar", "看，看见"),
        "select" to Pair("seleccionar, elegir", "选择，挑选"),
        "self" to Pair("uno mismo", "自己"),
        "sell" to Pair("vender, comercializar", "卖，销售"),
        "send" to Pair("enviar, remitir", "发，寄，送"),
        "sentence" to Pair("oración, frase", "句子"),
        "set" to Pair("establecer, fijar", "设定，置"),
        "set up" to Pair("instalar, fundar", "建立，设置"),
        "seven" to Pair("siete", "七"),
        "several" to Pair("varios, algunos", "数个，若干"),
        "shaanxi" to Pair("Shaanxi (provincia china)", "陕西省"),
        "shandong" to Pair("Shandong (provincia china)", "山东省"),
        "shanghai" to Pair("Shanghái", "上海"),
        "shanxi" to Pair("Shanxi (provincia china)", "山西省"),
        "she" to Pair("ella", "她"),
        "sheep" to Pair("oveja, cordero", "羊"),
        "ship" to Pair("barco, buque", "轮船，大船"),
        "shoes" to Pair("zapatos, calzado", "鞋子，鞋"),
        "shop" to Pair("tienda, negocio", "店铺，商店"),
        "short" to Pair("corto, bajo de estatura", "短，矮"),
        "show" to Pair("mostrar, exhibir", "出示，展示"),
        "sichuan" to Pair("Sichuan (provincia china)", "四川省"),
        "sick" to Pair("enfermo, indispuesto", "生病，有病"),
        "sing" to Pair("cantar", "唱，唱歌"),
        "singapore" to Pair("Singapur", "新加坡"),
        "sister" to Pair("hermana", "姐妹"),
        "sit" to Pair("sentarse, estar sentado", "坐，就座"),
        "six" to Pair("seis", "六"),
        "sky" to Pair("cielo, firmamento", "天空，天"),
        "sleep" to Pair("dormir, descansar", "睡，睡觉"),
        "slow" to Pair("lento, pausado", "慢，缓慢"),
        "small" to Pair("pequeño, menudo", "小，微小的"),
        "smart" to Pair("inteligente, listo", "聪明，聪慧"),
        "smile" to Pair("sonreír", "微笑，笑"),
        "snow" to Pair("nieve", "雪"),
        "so" to Pair("así que, por lo tanto", "所以，因此"),
        "soft" to Pair("blando, suave", "软，柔软"),
        "soil" to Pair("tierra, barro", "泥土，土地"),
        "solve" to Pair("resolver, solucionar", "解决，解答"),
        "some" to Pair("algunos, algo", "一些，有些"),
        "someone" to Pair("alguien", "某人，有人"),
        "sometimes" to Pair("a veces, de vez en cuando", "有时候，有时"),
        "son" to Pair("hijo", "儿子"),
        "song" to Pair("canción, tema musical", "歌，歌曲"),
        "soon" to Pair("pronto, en breve", "很快，不久"),
        "sorrow" to Pair("tristeza, dolor", "悲哀，悲伤"),
        "sound" to Pair("sonido, ruido", "声音"),
        "soup" to Pair("sopa, caldo", "汤"),
        "sour" to Pair("agrio, ácido", "酸"),
        "source" to Pair("fuente, principio", "来源，水源"),
        "south" to Pair("sur", "南，南方"),
        "south korea" to Pair("Corea del Sur", "韩国"),
        "spain" to Pair("España", "西班牙"),
        "speak" to Pair("hablar, pronunciar", "说，讲话"),
        "special" to Pair("especial, singular", "特别，特殊"),
        "spicy" to Pair("picante, sazonado", "辣"),
        "spring" to Pair("primavera", "春天，春季"),
        "stand" to Pair("estar de pie, levantarse", "站，站立"),
        "star" to Pair("estrella, astro", "星星，恒星"),
        "start" to Pair("empezar, comenzar", "开始，起步"),
        "state" to Pair("estado, condición", "状态，国家"),
        "station" to Pair("estación, terminal", "车站"),
        "stay" to Pair("permanecer, quedarse", "留，停留"),
        "stem" to Pair("tallo, tronco", "茎，干"),
        "still" to Pair("todavía, aún", "还，仍然"),
        "stop" to Pair("detenerse, cesar", "停，停止"),
        "store" to Pair("tienda, comercio", "商店，店铺"),
        "strange" to Pair("extraño, raro", "奇怪，陌生"),
        "stranger" to Pair("desconocido, forastero", "陌生人"),
        "street" to Pair("calle, rúa", "街道，马路"),
        "strike" to Pair("golpear, chocar", "打击，击中"),
        "stroke" to Pair("trazo caligráfico", "笔画"),
        "strokes" to Pair("trazos", "笔画"),
        "strong" to Pair("fuerte, potente", "强，强壮"),
        "student" to Pair("estudiante, alumno", "学生，学员"),
        "study" to Pair("estudiar, investigar", "学习，钻研"),
        "stupid" to Pair("estúpido, tonto", "笨，愚蠢"),
        "subway" to Pair("metro, subterráneo", "地铁"),
        "suffix" to Pair("sufijo", "后缀"),
        "sugar" to Pair("azúcar", "糖"),
        "summer" to Pair("verano", "夏天，夏季"),
        "sun" to Pair("sol", "太阳"),
        "supermarket" to Pair("supermercado", "超市，超级市场"),
        "support" to Pair("apoyar, respaldar", "支持，扶持"),
        "surname" to Pair("apellido", "姓氏"),
        "sweet" to Pair("dulce, acaramelado", "甜"),
        "swim" to Pair("nadar", "游泳"),
        "table" to Pair("mesa", "桌子"),
        "taiwan" to Pair("Taiwán", "台湾"),
        "take" to Pair("tomar, coger, llevar", "拿，取，带"),
        "take off" to Pair("quitarse (ropa)", "脱下"),
        "talk" to Pair("charlar, conversar", "说话，谈论"),
        "tall" to Pair("alto, crecido", "高挺"),
        "tap" to Pair("dar golpecito, pulsar", "轻拍，触碰"),
        "tasty" to Pair("sabroso, apetitoso", "可口，好吃"),
        "taxi" to Pair("taxi", "出租车"),
        "tea" to Pair("té", "茶"),
        "tea plant" to Pair("planta de té", "茶树"),
        "teach" to Pair("enseñar, instruir", "教，传授"),
        "teacher" to Pair("profesor, maestro", "老师，教师"),
        "teeth" to Pair("dientes", "牙齿"),
        "tell" to Pair("contar, relatar", "告诉，讲述"),
        "ten" to Pair("diez", "十"),
        "ten thousand" to Pair("diez mil", "万"),
        "thailand" to Pair("Tailandia", "泰国"),
        "thank" to Pair("agradecer, dar las gracias", "感谢，谢"),
        "theater" to Pair("teatro", "剧院，剧场"),
        "them" to Pair("ellos, les", "他们"),
        "then" to Pair("entonces, luego", "然后，那时"),
        "there" to Pair("allí, allá", "那里，那儿"),
        "they" to Pair("ellos, ellas", "他们"),
        "think" to Pair("pensar, reflexionar", "想，思考"),
        "third" to Pair("tercero", "第三"),
        "thirsty" to Pair("sediento, con sed", "渴"),
        "thousand" to Pair("mil", "千"),
        "three" to Pair("tres", "三"),
        "throw" to Pair("arrojar, lanzar", "扔，投掷"),
        "tibet" to Pair("Tíbet (región autónoma)", "西藏"),
        "time" to Pair("tiempo, momento", "时间，时候"),
        "tiny" to Pair("diminuto, menudo", "极小的，微细的"),
        "tired" to Pair("cansado, fatigado", "累，疲乏"),
        "to curry favor with" to Pair("ganarse el favor de", "讨好"),
        "to flatter" to Pair("adular, halagar", "奉承，谄媚"),
        "today" to Pair("hoy", "今天"),
        "together" to Pair("juntos, conjuntamente", "一起，共同"),
        "tomorrow" to Pair("mañana (día)", "明天"),
        "too" to Pair("demasiado; también", "太；也"),
        "tooth" to Pair("diente", "牙齿"),
        "touch" to Pair("tocar, palpar", "触摸，碰"),
        "town" to Pair("pueblo, villa", "城镇，集镇"),
        "train" to Pair("tren, ferrocarril", "火车"),
        "train station" to Pair("estación de tren", "火车站"),
        "travel" to Pair("viajar, recorrer", "旅游，旅行"),
        "tree" to Pair("árbol", "树，树木"),
        "trophy cup" to Pair("copa de trofeo", "奖杯"),
        "true" to Pair("verdadero, real", "真实，真"),
        "try" to Pair("intentar, probar", "试，尝试"),
        "two" to Pair("dos", "二"),
        "ugly" to Pair("feo, desagradable", "丑陋，难看"),
        "un-" to Pair("in-, des-", "非，不"),
        "uncle" to Pair("tío", "叔叔，伯父，舅舅"),
        "under" to Pair("debajo de, bajo", "在……下"),
        "understand" to Pair("comprender, entender", "理解，懂"),
        "united states" to Pair("Estados Unidos", "美国"),
        "university" to Pair("universidad", "大学，高等学府"),
        "up" to Pair("arriba, hacia arriba", "上，向上"),
        "us" to Pair("nosotros, nos", "我们"),
        "use" to Pair("usar, utilizar", "用，使用"),
        "variant" to Pair("variante", "异体，变体"),
        "vast" to Pair("vasto, inmenso", "辽阔，宽广"),
        "vegetable" to Pair("verdura, vegetal", "蔬菜，菜"),
        "vehicle" to Pair("vehículo, carruaje", "车辆，车"),
        "very" to Pair("muy, sumamente", "很，非常"),
        "vietnam" to Pair("Vietnam", "越南"),
        "village" to Pair("aldea, pueblo pequeño", "村庄，乡村"),
        "voice" to Pair("voz humana", "声音，嗓音"),
        "wait" to Pair("esperar, aguardar", "等，等待"),
        "waiter" to Pair("camarero, mesero", "服务员"),
        "wake" to Pair("despertar", "醒来"),
        "wake up" to Pair("despertar, despabilarse", "醒，醒来"),
        "walk" to Pair("caminar, andar", "走，步行"),
        "want" to Pair("querer, desear", "要，想要"),
        "warm" to Pair("tibio, cálido", "温暖，温和"),
        "wash" to Pair("lavar, asear", "洗，洗濯"),
        "watch" to Pair("reloj de pulsera", "表，手表"),
        "water" to Pair("agua", "水"),
        "we" to Pair("nosotros", "我们"),
        "weak" to Pair("débil, endeble", "弱，虚弱"),
        "wear" to Pair("vestir, llevar puesto", "穿，戴"),
        "weather" to Pair("clima, tiempo atmosférico", "天气"),
        "week" to Pair("semana", "星期，周"),
        "welcome" to Pair("dar la bienvenida", "欢迎"),
        "well" to Pair("bien, satisfactoriamente", "好，良好地"),
        "west" to Pair("oeste, occidente", "西，西方"),
        "what" to Pair("qué, cuál", "什么"),
        "when" to Pair("cuando", "当……时候"),
        "where" to Pair("dónde, adonde", "在哪里，何处"),
        "which" to Pair("cuál, qué", "哪个，哪一个"),
        "who" to Pair("quién", "谁"),
        "why" to Pair("por qué", "为什么"),
        "wife" to Pair("esposa, mujer", "妻子，老婆"),
        "win" to Pair("ganar, triunfar", "赢，获胜"),
        "wind" to Pair("viento, brisa", "风"),
        "window" to Pair("ventana", "窗户，窗"),
        "wine" to Pair("vino, licor", "酒，葡萄酒"),
        "winter" to Pair("invierno", "冬天，冬季"),
        "wish" to Pair("desear, augurar", "祝愿，祝"),
        "woman" to Pair("mujer, dama", "女人，女性"),
        "women" to Pair("mujeres", "女性"),
        "wood" to Pair("madera, bosque", "木材，树林"),
        "word" to Pair("palabra, vocablo", "词，字词"),
        "work" to Pair("trabajar, laborar", "工作，干活"),
        "world" to Pair("mundo, orbe", "世界"),
        "worry" to Pair("preocuparse", "担心，发愁"),
        "write" to Pair("escribir, redactar", "写，书写"),
        "wrong" to Pair("incorrecto, equivocado", "错，不对"),
        "xinjiang" to Pair("Xinjiang (región autónoma)", "新疆"),
        "year" to Pair("año", "年，年份"),
        "yesterday" to Pair("ayer", "昨天"),
        "yield" to Pair("rendir, ceder", "出产，屈服"),
        "you" to Pair("tú, usted", "你，您"),
        "young" to Pair("joven, mozo", "年轻，年幼"),
        "younger brother" to Pair("hermano menor", "弟弟"),
        "younger sister" to Pair("hermana menor", "妹妹"),
        "yuan" to Pair("yuan (moneda china)", "元，人民币元"),
        "yunnan" to Pair("Yunnan (provincia china)", "云南省"),
        "zero" to Pair("cero", "零"),
        "zhejiang" to Pair("Zhejiang (provincia china)", "浙江省")
    )

    // Glosario inverso para términos pedagógicos curados en español -> (Inglés, Chino)
    private val spanishGlossaryDirect: Map<String, Pair<String, String>> = mapOf(
        "bueno" to Pair("good", "好，优良"),
        "bien" to Pair("well, fine", "好，良好"),
        "agradable" to Pair("pleasant", "舒适，宜人"),
        "fácil de" to Pair("easy to", "容易，便于"),
        "muy" to Pair("very", "非常，很"),
        "estar de acuerdo" to Pair("to agree", "赞同，同意"),
        "recuperarse" to Pair("to recover", "痊愈，康复"),
        "persona" to Pair("person", "人，个体"),
        "ser humano" to Pair("human being", "人类"),
        "hombre" to Pair("man", "男人"),
        "mujer" to Pair("woman", "女人，女性"),
        "niño" to Pair("child", "孩子，儿童"),
        "hijo" to Pair("son", "儿子"),
        "hija" to Pair("daughter", "女儿"),
        "padre" to Pair("father", "父亲"),
        "madre" to Pair("mother", "母亲"),
        "amigo" to Pair("friend", "朋友"),
        "agua" to Pair("water", "水"),
        "fuego" to Pair("fire", "火"),
        "tierra" to Pair("earth, soil", "土地，大地"),
        "cielo" to Pair("sky, heaven", "天空，天"),
        "sol" to Pair("sun", "太阳"),
        "luna" to Pair("moon", "月亮"),
        "árbol" to Pair("tree", "树木"),
        "montaña" to Pair("mountain", "山峰"),
        "río" to Pair("river", "河流"),
        "comer" to Pair("to eat", "吃"),
        "beber" to Pair("to drink", "喝"),
        "ver" to Pair("to see", "看，看见"),
        "mirar" to Pair("to look", "看"),
        "escuchar" to Pair("to listen", "听"),
        "hablar" to Pair("to speak", "说话"),
        "decir" to Pair("to say", "说"),
        "leer" to Pair("to read", "阅读"),
        "escribir" to Pair("to write", "书写"),
        "aprender" to Pair("to learn", "学习"),
        "grande" to Pair("big, large", "大"),
        "pequeño" to Pair("small, little", "小"),
        "nuevo" to Pair("new", "新"),
        "viejo" to Pair("old", "旧，老"),
        "alto" to Pair("tall, high", "高"),
        "bajo" to Pair("low, short", "低，矮"),
        "rápido" to Pair("fast, quick", "快"),
        "lento" to Pair("slow", "慢"),
        "feliz" to Pair("happy", "快乐，高兴"),
        "triste" to Pair("sad", "伤心，悲伤"),
        "tiempo" to Pair("time, weather", "时间，天气"),
        "día" to Pair("day", "天，日"),
        "año" to Pair("year", "年"),
        "mes" to Pair("month", "月"),
        "hoy" to Pair("today", "今天"),
        "mañana" to Pair("tomorrow, morning", "明天，早上"),
        "ayer" to Pair("yesterday", "昨天"),
        "trabajo" to Pair("work, job", "工作"),
        "casa" to Pair("house, home", "家，房屋"),
        "escuela" to Pair("school", "学校"),
        "libro" to Pair("book", "书"),
        "puerta" to Pair("door", "门"),
        "ciudad" to Pair("city", "城市"),
        "país" to Pair("country", "国家")
    )

    fun formatL10nDefinition(rawDefinition: String, lang: AppLanguage): String {
        if (rawDefinition.isBlank()) return ""

        // 1. Limpieza de metadatos internos de CEDICT y preservación de simplificados
        var cleaned = rawDefinition
            .replace(Regex("""[一-鿿]+\|([一-鿿]+)""")) { it.groupValues[1] }
            .replace(Regex("""\bCL:[^;/|\n]+"""), "")
            .replace(Regex("""\[[^\]]+\]"""), "")
            .replace(Regex("""\|"""), ";")
            .trim()

        if (cleaned.startsWith("; ")) cleaned = cleaned.removePrefix("; ")
        if (cleaned.endsWith(" ;") || cleaned.endsWith(";")) cleaned = cleaned.removeSuffix(";").trim()

        return when (lang) {
            AppLanguage.SPANISH -> localizeToSpanish(cleaned)
            AppLanguage.CHINESE -> localizeToChinese(cleaned)
            AppLanguage.ENGLISH -> cleanEnglish(cleaned)
        }
    }

    private fun localizeToSpanish(text: String): String {
        val segments = text.split(Regex("""[/;]""")).map { it.trim() }.filter { it.isNotEmpty() }
        val translatedSegments = mutableListOf<String>()

        for (seg in segments) {
            var curText = seg
            var prefixTag = ""

            val tagMatch = Regex("""^(\([^)]+\))\s*(.*)$""").find(curText)
            if (tagMatch != null) {
                val rawTag = tagMatch.groupValues[1].lowercase()
                curText = tagMatch.groupValues[2].trim()
                prefixTag = tagMapEs[rawTag] ?: rawTag
            }

            val lower = curText.lowercase()

            if (lower.startsWith("abbr. for ")) {
                val target = curText.substring(10).trim()
                val res = if (prefixTag.isNotEmpty()) "$prefixTag abreviatura de $target" else "abreviatura de $target"
                translatedSegments.add(res)
                continue
            }
            if (lower.startsWith("surname ")) {
                val surname = curText.substring(8).trim()
                val res = if (prefixTag.isNotEmpty()) "$prefixTag apellido $surname" else "apellido $surname"
                translatedSegments.add(res)
                continue
            }
            if (lower.startsWith("classifier for ")) {
                val target = curText.substring(15).trim()
                val res = if (prefixTag.isNotEmpty()) "$prefixTag clasificador de $target" else "clasificador de $target"
                translatedSegments.add(res)
                continue
            }
            if (lower.startsWith("see ")) {
                val target = curText.substring(4).trim()
                val res = if (prefixTag.isNotEmpty()) "$prefixTag ver $target" else "ver $target"
                translatedSegments.add(res)
                continue
            }
            if (lower.startsWith("variant of ")) {
                val target = curText.substring(11).trim()
                val res = if (prefixTag.isNotEmpty()) "$prefixTag variante de $target" else "variante de $target"
                translatedSegments.add(res)
                continue
            }
            if (lower.startsWith("same as ")) {
                val target = curText.substring(8).trim()
                val res = if (prefixTag.isNotEmpty()) "$prefixTag igual que $target" else "igual que $target"
                translatedSegments.add(res)
                continue
            }
            if (lower.startsWith("also written ")) {
                val target = curText.substring(13).trim()
                val res = if (prefixTag.isNotEmpty()) "$prefixTag también escrito $target" else "también escrito $target"
                translatedSegments.add(res)
                continue
            }
            if (lower.startsWith("also pr. ")) {
                val target = curText.substring(9).trim()
                val res = if (prefixTag.isNotEmpty()) "$prefixTag también pronunciado $target" else "también pronunciado $target"
                translatedSegments.add(res)
                continue
            }
            if (lower.startsWith("taiwan pr. ")) {
                val target = curText.substring(11).trim()
                val res = if (prefixTag.isNotEmpty()) "$prefixTag pronunciación en Taiwán: $target" else "pronunciación en Taiwán: $target"
                translatedSegments.add(res)
                continue
            }
            if (lower.startsWith("used in ")) {
                val target = curText.substring(8).trim()
                val res = if (prefixTag.isNotEmpty()) "$prefixTag usado en $target" else "usado en $target"
                translatedSegments.add(res)
                continue
            }
            if (lower == "name of a river") {
                translatedSegments.add(if (prefixTag.isNotEmpty()) "$prefixTag nombre de un río" else "nombre de un río")
                continue
            }
            if (lower == "name of a mountain") {
                translatedSegments.add(if (prefixTag.isNotEmpty()) "$prefixTag nombre de una montaña" else "nombre de una montaña")
                continue
            }
            if (lower == "place name") {
                translatedSegments.add(if (prefixTag.isNotEmpty()) "$prefixTag nombre de lugar, topónimo" else "nombre de lugar, topónimo")
                continue
            }
            if (lower == "used in place names") {
                translatedSegments.add(if (prefixTag.isNotEmpty()) "$prefixTag usado en topónimos" else "usado en topónimos")
                continue
            }
            if (lower == "used in transliteration") {
                translatedSegments.add(if (prefixTag.isNotEmpty()) "$prefixTag usado en transliteración" else "usado en transliteración")
                continue
            }

            val subItems = curText.split(',').map { it.trim() }.filter { it.isNotEmpty() }
            val translatedSubs = subItems.map { sub ->
                val cleanSub = sub.removePrefix("to ").removePrefix("a ").removePrefix("an ").removePrefix("the ").trim().lowercase()
                val match = glossaryDirect[sub.lowercase()] ?: glossaryDirect[cleanSub]
                match?.first ?: spanishGlossaryDirect[cleanSub]?.first?.let { sub } ?: sub
            }

            if (translatedSubs.isNotEmpty()) {
                val joined = translatedSubs.distinct().joinToString(", ")
                val fullSeg = if (prefixTag.isNotEmpty()) "$prefixTag $joined" else joined
                translatedSegments.add(fullSeg)
            }
        }

        return translatedSegments.distinct().joinToString("; ")
    }

    private fun localizeToChinese(text: String): String {
        val segments = text.split(Regex("""[/;]""")).map { it.trim() }.filter { it.isNotEmpty() }
        val translatedSegments = mutableListOf<String>()

        for (seg in segments) {
            var curText = seg
            var prefixTag = ""

            val tagMatch = Regex("""^(\([^)]+\))\s*(.*)$""").find(curText)
            if (tagMatch != null) {
                val rawTag = tagMatch.groupValues[1].lowercase()
                curText = tagMatch.groupValues[2].trim()
                prefixTag = tagMapZh[rawTag] ?: rawTag
            }

            val lower = curText.lowercase()

            if (lower.startsWith("abbr. for ")) {
                val target = curText.substring(10).trim()
                val res = if (prefixTag.isNotEmpty()) "$prefixTag 简称$target" else "简称$target"
                translatedSegments.add(res)
                continue
            }
            if (lower.startsWith("surname ")) {
                val surname = curText.substring(8).trim()
                val res = if (prefixTag.isNotEmpty()) "$prefixTag 姓氏$surname" else "姓氏$surname"
                translatedSegments.add(res)
                continue
            }
            if (lower.startsWith("classifier for ")) {
                val target = curText.substring(15).trim()
                val res = if (prefixTag.isNotEmpty()) "$prefixTag 量词：$target" else "量词：$target"
                translatedSegments.add(res)
                continue
            }
            if (lower.startsWith("see ")) {
                val target = curText.substring(4).trim()
                val res = if (prefixTag.isNotEmpty()) "$prefixTag 参见：$target" else "参见：$target"
                translatedSegments.add(res)
                continue
            }
            if (lower.startsWith("variant of ")) {
                val target = curText.substring(11).trim()
                val res = if (prefixTag.isNotEmpty()) "$prefixTag 异体字：$target" else "异体字：$target"
                translatedSegments.add(res)
                continue
            }
            if (lower.startsWith("same as ")) {
                val target = curText.substring(8).trim()
                val res = if (prefixTag.isNotEmpty()) "$prefixTag 同：$target" else "同：$target"
                translatedSegments.add(res)
                continue
            }
            if (lower.startsWith("also written ")) {
                val target = curText.substring(13).trim()
                val res = if (prefixTag.isNotEmpty()) "$prefixTag 亦作：$target" else "亦作：$target"
                translatedSegments.add(res)
                continue
            }
            if (lower.startsWith("also pr. ")) {
                val target = curText.substring(9).trim()
                val res = if (prefixTag.isNotEmpty()) "$prefixTag 亦读作：$target" else "亦读作：$target"
                translatedSegments.add(res)
                continue
            }
            if (lower.startsWith("taiwan pr. ")) {
                val target = curText.substring(11).trim()
                val res = if (prefixTag.isNotEmpty()) "$prefixTag 台湾读音：$target" else "台湾读音：$target"
                translatedSegments.add(res)
                continue
            }
            if (lower.startsWith("used in ")) {
                val target = curText.substring(8).trim()
                val res = if (prefixTag.isNotEmpty()) "$prefixTag 用于：$target" else "用于：$target"
                translatedSegments.add(res)
                continue
            }
            if (lower == "name of a river") {
                translatedSegments.add(if (prefixTag.isNotEmpty()) "$prefixTag 河流名" else "河流名")
                continue
            }
            if (lower == "name of a mountain") {
                translatedSegments.add(if (prefixTag.isNotEmpty()) "$prefixTag 山峰名" else "山峰名")
                continue
            }
            if (lower == "place name") {
                translatedSegments.add(if (prefixTag.isNotEmpty()) "$prefixTag 地名" else "地名")
                continue
            }
            if (lower == "used in place names") {
                translatedSegments.add(if (prefixTag.isNotEmpty()) "$prefixTag 地名用字" else "地名用字")
                continue
            }
            if (lower == "used in transliteration") {
                translatedSegments.add(if (prefixTag.isNotEmpty()) "$prefixTag 音译专用字" else "音译专用字")
                continue
            }

            val subItems = curText.split(',').map { it.trim() }.filter { it.isNotEmpty() }
            val translatedSubs = subItems.map { sub ->
                val cleanSub = sub.removePrefix("to ").removePrefix("a ").removePrefix("an ").removePrefix("the ").trim().lowercase()
                val match = glossaryDirect[sub.lowercase()] ?: glossaryDirect[cleanSub]
                match?.second ?: spanishGlossaryDirect[cleanSub]?.second ?: sub
            }

            if (translatedSubs.isNotEmpty()) {
                val joined = translatedSubs.distinct().joinToString("，")
                val fullSeg = if (prefixTag.isNotEmpty()) "$prefixTag $joined" else joined
                translatedSegments.add(fullSeg)
            }
        }

        return translatedSegments.distinct().joinToString("；")
    }

    private fun cleanEnglish(text: String): String {
        val segments = text.split(Regex("""[/;]""")).map { it.trim() }.filter { it.isNotEmpty() }
        val translatedSegments = mutableListOf<String>()

        for (seg in segments) {
            val subItems = seg.split(',').map { it.trim() }.filter { it.isNotEmpty() }
            val translatedSubs = subItems.map { sub ->
                val cleanSub = sub.trim().lowercase()
                spanishGlossaryDirect[cleanSub]?.first ?: sub
            }
            translatedSegments.add(translatedSubs.joinToString(", "))
        }

        return translatedSegments.joinToString("; ")
            .replace(Regex("""\s+"""), " ")
            .replace(Regex("""\s*;\s*"""), "; ")
            .replace(Regex("""\s*/\s*"""), " / ")
            .trim()
    }

    // Traducciones canónicas de las oraciones de ejemplo para los caracteres HSK curados
    private val curatedSentencesEn: Map<String, String> = mapOf(
        "你好，很高兴认识你。" to "Hello, nice to meet you.",
        "你是哪国人？" to "Where are you from?",
        "你的中文非常流利。" to "Your Chinese is very fluent.",
        "今天天气很好，阳光明媚。" to "The weather is very good today, sunny and bright.",
        "这本汉语书非常好看。" to "This Chinese book is very interesting and enjoyable.",
        "太好了，我们明天一起去！" to "Great, let's go together tomorrow!",
        "我是留学生，正在中国学习汉语。" to "I am an international student studying Chinese in China.",
        "我非常喜欢吃中国菜。" to "I really like eating Chinese food.",
        "请告诉我你的电话号码。" to "Please tell me your phone number.",
        "图书馆里有很多安静看书的人。" to "There are many people quietly reading in the library.",
        "他是一个非常善良热心的人。" to "He is a very kind and enthusiastic person.",
        "中国有十四亿多人口。" to "China has a population of over 1.4 billion people.",
        "父母非常爱他们的孩子。" to "Parents love their children deeply.",
        "我爱看电影，也爱旅行。" to "I love watching movies, and I also love traveling.",
        "爱心能够温暖每一个人。" to "Love and kindness can warm everyone.",
        "工作累了，请喝杯茶休息一下。" to "When tired from work, please have a cup of tea and take a rest.",
        "我们周末去公园休闲散步。" to "We go to the park for a leisurely walk on weekends.",
        "休假期间他去海边度假了。" to "During his vacation, he went to the seaside.",
        "窗外明亮的阳光照进房间。" to "Bright sunlight shines into the room outside the window.",
        "明天早上八点我们在校门口集合。" to "Tomorrow at 8:00 AM we will meet at the school gate.",
        "请你向大家说明这件事情的经过。" to "Please explain to everyone what happened.",
        "我爱我的家人，家庭非常温馨。" to "I love my family, my home is very warm and cozy.",
        "欢迎来到中国，这里是你的新家。" to "Welcome to China, this is your new home.",
        "大家一起努力把工作做好。" to "Let's all work hard together to do the job well.",
        "那个勇敢坚毅的男人是位消防员。" to "That brave and resolute man is a firefighter.",
        "男女平等是现代社会的重要原则。" to "Gender equality is an important principle of modern society.",
        "男孩子们在足球场上奔跑踢球。" to "The boys are running and playing soccer on the field.",
        "我看过很多关于中国历史的书籍。" to "I have read many books about Chinese history.",
        "请大家看黑板，注意这个语法点。" to "Please look at the blackboard and pay attention to this grammar point.",
        "你看这件红色旗袍漂亮不漂亮？" to "Do you think this red qipao is pretty or not?",
        "我们在大学里认真学习汉语。" to "We study Chinese diligently at the university.",
        "好好学习，天天向上。" to "Study hard and make progress every day.",
        "学校图书馆藏书非常丰富。" to "The school library has an extensive book collection.",
        "中文是世界上使用人数最多的语言之一。" to "Chinese is one of the most widely spoken languages in the world.",
        "中国拥有悠久灿烂的传统文化。" to "China possesses a long and splendid traditional culture.",
        "中国菜不仅美味，而且色香味俱全。" to "Chinese food is not only delicious, but also fragrant and appealing.",
        "我们班有来自不同国家的朋友。" to "Our class has friends from many different countries.",
        "国际交流促进了世界文明互鉴。" to "International exchange fosters mutual learning between world civilizations."
    )

    fun formatL10nExampleSentence(chinese: String, @Suppress("UNUSED_PARAMETER") pinyin: String, spanish: String, lang: AppLanguage): String {
        return when (lang) {
            AppLanguage.SPANISH -> spanish
            AppLanguage.ENGLISH -> {
                curatedSentencesEn[chinese] ?: when {
                    spanish.contains("En la vida cotidiana, se utiliza con frecuencia la palabra o expresión") -> {
                        "In daily conversation, the expression '$chinese' is frequently used in spoken Mandarin."
                    }
                    spanish.contains("Podemos aprender en el texto de clase el uso práctico de") -> {
                        "In classroom lessons, we study the practical communicative usage of '$chinese'."
                    }
                    spanish.contains("Esta oración ejemplifica la estructura gramatical habitual de") -> {
                        "This example illustrates the standard grammatical structure of '$chinese'."
                    }
                    else -> spanish
                }
            }
            AppLanguage.CHINESE -> {
                "课文实用情境例句，展示「$chinese」在日常交流中的标准用法。"
            }
        }
    }

    fun formatMnemonicStory(info: EnhancedCharacterInfo, lang: AppLanguage): String {
        val def = formatL10nDefinition(info.primaryDefinition, lang)
        val c1 = info.components.firstOrNull()
        val c2 = info.components.getOrNull(1)

        val c1Meaning = if (c1 != null) getRadicalMeaning(c1.component, c1.meaning, lang) else ""
        val c2Meaning = if (c2 != null) getRadicalMeaning(c2.component, c2.meaning, lang) else ""

        return when (lang) {
            AppLanguage.SPANISH -> {
                if (c1 != null && c2 != null) {
                    "Para recordar '${info.simplified}' (${info.pinyin}): conecta el componente '${c1.component}' ($c1Meaning) con '${c2.component}' ($c2Meaning). Juntos estructuran el concepto de '$def'."
                } else if (c1 != null) {
                    "Para recordar '${info.simplified}' (${info.pinyin}): asocia el radical '${c1.component}' ($c1Meaning) como la clave semántica para evocar el significado de '$def'."
                } else {
                    "Para recordar '${info.simplified}' (${info.pinyin}): visualiza la silueta gráfica asociada al significado de '$def' en la armonía de sus trazos primarios."
                }
            }
            AppLanguage.ENGLISH -> {
                if (c1 != null && c2 != null) {
                    "To remember '${info.simplified}' (${info.pinyin}): connect component '${c1.component}' ($c1Meaning) with '${c2.component}' ($c2Meaning). Together they evoke '$def'."
                } else if (c1 != null) {
                    "To remember '${info.simplified}' (${info.pinyin}): visualize semantic radical '${c1.component}' ($c1Meaning) guiding memory towards '$def'."
                } else {
                    "To remember '${info.simplified}' (${info.pinyin}): picture the stroke flow conveying the core meaning of '$def'."
                }
            }
            AppLanguage.CHINESE -> {
                if (c1 != null && c2 != null) {
                    "记忆口诀「${info.simplified}」(${info.pinyin})：联想构件「${c1.component}」（$c1Meaning）与「${c2.component}」（$c2Meaning），二者会意相合，构成「$def」之意。"
                } else if (c1 != null) {
                    "记忆口诀「${info.simplified}」(${info.pinyin})：以偏旁部首「${c1.component}」（$c1Meaning）为核心义符，提示「$def」之语义脉络。"
                } else {
                    "记忆口诀「${info.simplified}」(${info.pinyin})：观摩笔画形态，象形表意，直观呈现「$def」之本义。"
                }
            }
        }
    }

    fun formatEtymology(info: EnhancedCharacterInfo, lang: AppLanguage): String {
        val radicalMeaning = getRadicalMeaning(info.radical, "", lang)
        return when (lang) {
            AppLanguage.SPANISH -> {
                "Compuesto fono-semántico (形声): El radical '${info.radical}' ($radicalMeaning) establece el campo semántico, mientras que el componente complementario aporta la estructura fonética e histórica."
            }
            AppLanguage.ENGLISH -> {
                "Phono-semantic compound (形声): The radical '${info.radical}' ($radicalMeaning) defines the semantic domain, while the phonetic component guides pronunciation and historical usage."
            }
            AppLanguage.CHINESE -> {
                "形声会意造字法：偏旁「${info.radical}」（$radicalMeaning）统摄语义范畴，声旁表征古音韵律与结构演化。"
            }
        }
    }

    fun formatEvolution(info: EnhancedCharacterInfo, lang: AppLanguage): String {
        return when (lang) {
            AppLanguage.SPANISH -> {
                "Evolución estilística: Inscripciones oraculares Jiaguwen (甲骨文) ➔ Bronce Jinwen (金文) ➔ Escritura de Sello (篆书) ➔ Caligrafía Clerical (隶书) ➔ Hanzi Regular y Simplificado '${info.simplified}'."
            }
            AppLanguage.ENGLISH -> {
                "Stylistic evolution: Oracle Bone script (Jiaguwen 甲骨文) ➔ Bronze inscriptions (Jinwen 金文) ➔ Seal script (Zhuanshu 篆书) ➔ Clerical calligraphy (Lishu 隶书) ➔ Standard Simplified Hanzi '${info.simplified}'."
            }
            AppLanguage.CHINESE -> {
                "书体源流演变：商周甲骨文 (𓇿) ➔ 青铜器金文铭文 ➔ 秦汉大篆小篆 ➔ 汉魏隶书楷化 ➔ 现代规范简化字「${info.simplified}」。"
            }
        }
    }

    fun formatIllustrationHelp(info: EnhancedCharacterInfo, lang: AppLanguage): String {
        val def = formatL10nDefinition(info.primaryDefinition, lang)
        val radicalMeaning = getRadicalMeaning(info.radical, "", lang)
        return when (lang) {
            AppLanguage.SPANISH -> {
                "La tarjeta resalta visualmente la estructura de '${info.simplified}' compuesta por el radical '${info.radical}' ($radicalMeaning), guiando la retención óptica hacia el significado '$def'."
            }
            AppLanguage.ENGLISH -> {
                "This card visually highlights the structure of '${info.simplified}' composed of radical '${info.radical}' ($radicalMeaning), guiding optical retention toward the meaning '$def'."
            }
            AppLanguage.CHINESE -> {
                "本图解突出「${info.simplified}」以偏旁部首「${info.radical}」（$radicalMeaning）为基准的间架结构，强化视觉联想以快速掌握「$def」之语义。"
            }
        }
    }
}
