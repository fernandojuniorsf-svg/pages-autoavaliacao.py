import streamlit as st
import pandas as pd
import datetime
import os
import json

# ============================================
# CONFIGURACAO INICIAL
# ============================================

st.set_page_config(
    page_title="EUA8 - Formularios",
    page_icon="📦",
    layout="centered"
)

# CSS CUSTOMIZADO - CORES AMAZON
st.markdown("""
<style>
    /* Header */
    .stApp header {
        background-color: #232F3E;
    }

    /* Botoes */
    .stButton > button {
        background-color: #FF9900;
        color: #232F3E;
        font-weight: bold;
        border: none;
        border-radius: 8px;
    }
    .stButton > button:hover {
        background-color: #E88B00;
        color: #232F3E;
    }

    /* Form submit */
    .stFormSubmitButton > button {
        background-color: #FF9900;
        color: #232F3E;
        font-weight: bold;
        border: none;
        border-radius: 8px;
        padding: 0.5rem 1rem;
    }

    /* Titulos */
    h1, h2, h3 {
        color: #232F3E;
    }

    /* Sidebar */
    section[data-testid="stSidebar"] {
        background-color: #232F3E;
    }
    section[data-testid="stSidebar"] * {
        color: #FFFFFF;
    }

    /* Tabs */
    .stTabs [data-baseweb="tab"] {
        color: #232F3E;
    }
    .stTabs [aria-selected="true"] {
        border-bottom-color: #FF9900;
    }

    /* Divider */
    hr {
        border-color: #FF9900;
    }

    /* Logo header customizado */
    .amazon-header {
        background-color: #232F3E;
        padding: 1rem;
        border-radius: 10px;
        text-align: center;
        margin-bottom: 1rem;
    }
    .amazon-header h1 {
        color: #FF9900;
        margin: 0;
    }
    .amazon-header p {
        color: #FFFFFF;
        margin: 0;
    }

    /* Cards */
    .metric-card {
        background-color: #232F3E;
        padding: 1rem;
        border-radius: 10px;
        text-align: center;
        color: #FFFFFF;
    }
    .metric-card h2 {
        color: #FF9900;
        margin: 0;
    }
</style>
""", unsafe_allow_html=True)

DATA_DIR = "data"
os.makedirs(DATA_DIR, exist_ok=True)

RESPOSTAS_FILE = os.path.join(DATA_DIR, "respostas.csv")
PERGUNTAS_FILE = os.path.join(DATA_DIR, "perguntas.json")
NOTAS_FILE = os.path.join(DATA_DIR, "notas_atividades.csv")

# ============================================
# USUARIOS E SENHAS
# ============================================

USUARIOS = {
    "fernando": {"senha": "fer123", "nome": "Fernando Junior", "role": "admin"},
    "mayra": {"senha": "may123", "nome": "Mayra", "role": "associado"},
    "carol": {"senha": "car123", "nome": "Carol", "role": "associado"},
    "amanda": {"senha": "ama123", "nome": "Amanda", "role": "associado"},
    "helen": {"senha": "hel123", "nome": "Helen", "role": "associado"},
    "nicole": {"senha": "nic123", "nome": "Nicole", "role": "associado"},
    "nicole2": {"senha": "nic123", "nome": "Nicole", "role": "associado"},
    "fernanda": {"senha": "fer123", "nome": "Fernanda", "role": "associado"},
    "byanca": {"senha": "bya123", "nome": "Byanca", "role": "associado"},
    "kalebe": {"senha": "kal123", "nome": "Kalebe", "role": "associado"},
}

# ============================================
# ATIVIDADES PARA NOTA
# ============================================

ATIVIDADES = [
    "Pick to Buffer",
    "Stow",
    "Receiving (Recebimento)",
    "Unloading (Descarregamento)",
    "Loading (Carregamento)",
    "Yard Marshall (Patio)",
    "Spider / Fechamento",
    "Comunicacao pelo radio",
    "Organizacao (5S)",
    "Seguranca",
]

# ============================================
# PERGUNTAS PADRAO
# ============================================

PERGUNTAS_PADRAO = {
    "texto": [
        {"id": "pontos_fortes", "pergunta": "Quais sao seus 3 pontos fortes na operacao?", "hint": "O que voce faz bem? No que se destaca?"},
        {"id": "pontos_melhorar", "pergunta": "O que voce sente que precisa melhorar?", "hint": "Seja honesto. Isso me ajuda a te apoiar."},
        {"id": "acao_melhorar", "pergunta": "O que VOCE pode fazer pra melhorar nesses pontos?", "hint": "Pense em acoes concretas que dependem de voce."},
        {"id": "equipe_melhorar", "pergunta": "O que a equipe/operacao pode melhorar no geral?", "hint": "O que voce enxerga que poderia ser diferente?"},
        {"id": "feedback_lider", "pergunta": "O que eu (Fernando) posso fazer pra te ajudar?", "hint": "Pode ser qualquer coisa: treinamento, posicionamento, feedback..."},
        {"id": "mensagem_time", "pergunta": "Mensagem pro time (opcional)", "hint": "Algo que queira compartilhar com todos..."},
    ],
    "escala": [
        {"id": "comunicacao", "pergunta": "Comunicacao com o time"},
        {"id": "urgencia", "pergunta": "Senso de urgencia"},
        {"id": "qualidade", "pergunta": "Qualidade (atencao a erros)"},
        {"id": "trabalho_equipe", "pergunta": "Trabalho em equipe"},
    ],
    "selecao": [
        {"id": "posicao_aprender", "pergunta": "Posicao que gostaria de aprender", "opcoes": [
            "Pick to Buffer", "Stow", "Receiver", "Unloader",
            "Yard Marshall", "Spider", "Carregamento", "Todas!",
            "Estou bem na minha posicao atual"
        ]}
    ]
}


def carregar_perguntas():
    if os.path.exists(PERGUNTAS_FILE):
        with open(PERGUNTAS_FILE, "r", encoding="utf-8") as f:
            return json.load(f)
    return PERGUNTAS_PADRAO


def salvar_perguntas(perguntas):
    with open(PERGUNTAS_FILE, "w", encoding="utf-8") as f:
        json.dump(perguntas, f, ensure_ascii=False, indent=2)


def salvar_resposta(dados, arquivo):
    df_novo = pd.DataFrame([dados])
    if os.path.exists(arquivo):
        df_existente = pd.read_csv(arquivo)
        df_final = pd.concat([df_existente, df_novo], ignore_index=True)
    else:
        df_final = df_novo
    df_final.to_csv(arquivo, index=False)


def carregar_respostas(arquivo):
    if os.path.exists(arquivo):
        return pd.read_csv(arquivo)
    return pd.DataFrame()


# ============================================
# TELA DE LOGIN
# ============================================

def tela_login():
    st.markdown("""
    <div class="amazon-header">
        <h1>📦 EUA8</h1>
        <p>First Mile Operations</p>
    </div>
    """, unsafe_allow_html=True)

    st.markdown("### 🔐 Login")
    st.caption("Digite seu usuario e senha fornecidos pelo Fernando.")

    col1, col2, col3 = st.columns([1, 2, 1])
    with col2:
        usuario = st.text_input("Usuario", placeholder="Ex: mayra").lower().strip()
        senha = st.text_input("Senha", type="password", placeholder="Sua senha")

        if st.button("Entrar", use_container_width=True):
            if usuario in USUARIOS and USUARIOS[usuario]["senha"] == senha:
                st.session_state["logado"] = True
                st.session_state["usuario"] = usuario
                st.session_state["nome"] = USUARIOS[usuario]["nome"]
                st.session_state["role"] = USUARIOS[usuario]["role"]
                st.rerun()
            else:
                st.error("Usuario ou senha incorretos.")


# ============================================
# FORMULARIO 1: AUTOAVALIACAO
# ============================================

def form_autoavaliacao():
    perguntas = carregar_perguntas()

    st.markdown("### 📋 Autoavaliacao")
    st.caption("Responda com sinceridade. Suas respostas sao confidenciais.")

    with st.form("form_autoavaliacao"):
        respostas = {}
        respostas["nome"] = st.session_state["nome"]
        respostas["data"] = str(datetime.date.today())
        respostas["hora"] = datetime.datetime.now().strftime("%H:%M")

        for p in perguntas["texto"]:
            respostas[p["id"]] = st.text_area(
                p["pergunta"],
                placeholder=p.get("hint", ""),
                height=80
            )

        st.divider()
        for p in perguntas["selecao"]:
            respostas[p["id"]] = st.selectbox(p["pergunta"], p["opcoes"])

        st.divider()
        st.markdown("**Autoavaliacao (1 a 5)**")
        for p in perguntas["escala"]:
            respostas[p["id"]] = st.slider(p["pergunta"], 1, 5, 3)

        enviado = st.form_submit_button("Enviar Autoavaliacao", use_container_width=True)

        if enviado:
            if not respostas.get("pontos_fortes"):
                st.error("Preencha pelo menos os pontos fortes.")
            else:
                salvar_resposta(respostas, RESPOSTAS_FILE)
                st.success("Resposta enviada com sucesso! 🎉")
                st.balloons()


# ============================================
# FORMULARIO 2: NOTA POR ATIVIDADE
# ============================================

def form_notas_atividades():
    st.markdown("### ⭐ Nota por Atividade")
    st.caption("De uma nota de 1 a 5 para cada atividade e uma breve justificativa.")

    with st.form("form_notas"):
        respostas = {}
        respostas["nome"] = st.session_state["nome"]
        respostas["data"] = str(datetime.date.today())
        respostas["hora"] = datetime.datetime.now().strftime("%H:%M")

        for atividade in ATIVIDADES:
            st.markdown(f"**{atividade}**")
            col1, col2 = st.columns([1, 3])
            with col1:
                respostas[f"nota_{atividade}"] = st.selectbox(
                    "Nota",
                    [1, 2, 3, 4, 5],
                    index=2,
                    key=f"nota_{atividade}",
                    label_visibility="collapsed"
                )
            with col2:
                respostas[f"justificativa_{atividade}"] = st.text_input(
                    "Por que?",
                    placeholder="Breve justificativa...",
                    key=f"just_{atividade}",
                    label_visibility="collapsed"
                )
            st.divider()

        enviado = st.form_submit_button("Enviar Notas", use_container_width=True)

        if enviado:
            salvar_resposta(respostas, NOTAS_FILE)
            st.success("Notas enviadas com sucesso! 🎉")
            st.balloons()


# ============================================
# TELA DO ASSOCIADO
# ============================================

def tela_associado():
    st.markdown("""
    <div class="amazon-header">
        <h1>📦 EUA8</h1>
        <p>First Mile Operations</p>
    </div>
    """, unsafe_allow_html=True)

    st.markdown(f"### 👋 Ola, {st.session_state['nome']}!")
    st.caption("Escolha o formulario:")

    opcao = st.radio(
        "Formularios:",
        ["📋 Autoavaliacao", "⭐ Nota por Atividade"],
        label_visibility="collapsed",
        horizontal=True
    )

    st.divider()

    if opcao == "📋 Autoavaliacao":
        form_autoavaliacao()
    elif opcao == "⭐ Nota por Atividade":
        form_notas_atividades()

    st.divider()
    if st.button("🚪 Sair"):
        st.session_state.clear()
        st.rerun()


# ============================================
# PAINEL ADMIN
# ============================================

def tela_admin():
    st.markdown("""
    <div class="amazon-header">
        <h1>📦 EUA8 - Admin</h1>
        <p>Painel de Gestao</p>
    </div>
    """, unsafe_allow_html=True)

    st.markdown(f"**Bem-vindo, {st.session_state['nome']}!**")

    tab1, tab2, tab3, tab4 = st.tabs([
        "📋 Autoavaliacao",
        "⭐ Notas",
        "✏️ Editar Perguntas",
        "👥 Usuarios"
    ])

    with tab1:
        df = carregar_respostas(RESPOSTAS_FILE)
        if df.empty:
            st.info("Nenhuma resposta de autoavaliacao ainda.")
        else:
            st.metric("Total de Respostas", len(df))
            nomes = ["Todos"] + sorted(df["nome"].unique().tolist())
            filtro = st.selectbox("Filtrar por nome:", nomes, key="filtro_auto")
            if filtro != "Todos":
                df = df[df["nome"] == filtro]
            st.dataframe(df, use_container_width=True)
            csv = df.to_csv(index=False).encode("utf-8")
            st.download_button(
                "📥 Exportar CSV",
                csv,
                f"autoavaliacao_{datetime.date.today()}.csv",
                "text/csv",
                use_container_width=True
            )

    with tab2:
        df2 = carregar_respostas(NOTAS_FILE)
        if df2.empty:
            st.info("Nenhuma nota ainda.")
        else:
            st.metric("Total de Respostas", len(df2))
            nomes2 = ["Todos"] + sorted(df2["nome"].unique().tolist())
            filtro2 = st.selectbox("Filtrar por nome:", nomes2, key="filtro_notas")
            if filtro2 != "Todos":
                df2 = df2[df2["nome"] == filtro2]
            st.dataframe(df2, use_container_width=True)
            csv2 = df2.to_csv(index=False).encode("utf-8")
            st.download_button(
                "📥 Exportar CSV",
                csv2,
                f"notas_{datetime.date.today()}.csv",
                "text/csv",
                use_container_width=True
            )

    with tab3:
        st.caption("Edite as perguntas da autoavaliacao.")
        perguntas = carregar_perguntas()

        st.markdown("**Perguntas de Texto**")
        novas_texto = []
        for i, p in enumerate(perguntas["texto"]):
            with st.expander(f"Pergunta {i+1}: {p['pergunta'][:50]}..."):
                nova_pergunta = st.text_input(f"Pergunta {i+1}", value=p["pergunta"], key=f"pt_{i}")
                novo_hint = st.text_input(f"Dica {i+1}", value=p.get("hint", ""), key=f"ph_{i}")
                novas_texto.append({"id": p["id"], "pergunta": nova_pergunta, "hint": novo_hint})

        st.markdown("**Perguntas de Escala (1-5)**")
        novas_escala = []
        for i, p in enumerate(perguntas["escala"]):
            nova = st.text_input(f"Escala {i+1}", value=p["pergunta"], key=f"pe_{i}")
            novas_escala.append({"id": p["id"], "pergunta": nova})

        if st.button("Salvar Perguntas", use_container_width=True):
            perguntas["texto"] = novas_texto
            perguntas["escala"] = novas_escala
            salvar_perguntas(perguntas)
            st.success("Perguntas atualizadas!")

    with tab4:
        st.caption("Usuarios cadastrados:")
        dados_users = []
        for user, info in USUARIOS.items():
            dados_users.append({
                "Usuario": user,
                "Nome": info["nome"],
                "Senha": info["senha"],
                "Tipo": info["role"]
            })
        st.dataframe(pd.DataFrame(dados_users), use_container_width=True)

    st.divider()
    if st.button("🚪 Sair"):
        st.session_state.clear()
        st.rerun()


# ============================================
# APP PRINCIPAL
# ============================================

def main():
    if "logado" not in st.session_state:
        tela_login()
    elif st.session_state["role"] == "admin":
        tela_admin()
    else:
        tela_associado()


if __name__ == "__main__":
    main()
