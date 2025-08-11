# ========================================
# FILENAME: aivlebigproject/init.sh
# 역할 : 로컬 환경 초기 패키지 설치.
# ========================================

# 1. 시스템 기본 패키지 설치
sudo apt-get update
sudo apt-get install net-tools
sudo apt install iputils-ping
sudo apt install httpie

# 2. Kubernetes CLI (kubectl) 설치
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl

# 3. Node.js 버전 관리자 (nvm) 및 Node.js 설치
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.38.0/install.sh | bash
. ~/.nvm/nvm.sh
nvm install 14.19.0 && nvm use 14.19.0
export NODE_OPTIONS=--openssl-legacy-provider

echo "✅ Initialization complete!"