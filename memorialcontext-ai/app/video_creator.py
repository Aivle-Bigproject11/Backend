# ============================================================================
# 메인 비디오 생성
# ============================================================================

import os

from moviepy import TextClip, AudioFileClip, ImageClip, CompositeVideoClip, ColorClip, VideoFileClip
from moviepy import afx
from app.azure_blob import upload_video_to_blob
from app.config import TEST_MODE,  VIDEO_SIZE, FONT_PATH
from app.scenario import generate_scenario
from app.utils import create_directories


class MemorialVideoCreator:
    """추모영상 생성 클래스"""

    def __init__(self, base_path="./", memorialId="test_test", videoId=None):
        self.base_path = base_path
        self.image_dir = os.path.join(base_path, "images")
        self.music_dir = os.path.join(base_path, "music")
        self.outro_dir = os.path.join(base_path, "outro")
        self.output_dir = os.path.join(base_path, "output")
        self.video_size = VIDEO_SIZE
        self.memorialId = memorialId
        self.video_id = videoId


        # 디렉토리 생성
        create_directories(
            self.base_path, self.image_dir, self.music_dir,
            self.outro_dir, self.output_dir
        )


    def create_subtitle_clip(self, subtitle, duration):
        """자막 클립 생성"""
        return TextClip(
            text=subtitle,
            font=FONT_PATH,
            font_size=44,
            color='white',
            stroke_color='black',
            stroke_width=4,
            method='caption',
            text_align='center',
            size=(self.video_size[0] - 100, None)  # 좌우 여백 50px씩
        ).with_duration(duration).with_position(('center', self.video_size[1] - 150))

    def create_scene_clip(self, scene):
        """개별 장면 클립 생성"""
        subtitle = scene['subtitle']
        duration = scene['duration']

        if scene['scene_type'] == 'outro':
            return self._create_outro_clip(subtitle, duration)
        else:
            return self._create_main_clip(scene, subtitle, duration)

    def _create_outro_clip(self, subtitle, duration):
        outro_clip_path = os.path.join(self.outro_dir, "runway_result.mp4")
        oc = VideoFileClip(outro_clip_path)

        # 1) 안전 자르기(종료 시점 - 아주 작은 여유)
        safe_end = min(duration, oc.duration - 0.02)
        oc = oc.subclipped(0, max(0, safe_end))

        # 2) 남는 시간은 마지막 프레임 freeze
        if safe_end < duration:
            tail = oc.to_ImageClip().with_duration(duration - safe_end)
            oc = CompositeVideoClip([oc.with_position(('center', 'center')),
                                     tail.with_start(safe_end)], size=self.video_size)

        oc = oc.resized(height=self.video_size[1]).with_duration(duration)

        subtitle_clip = self.create_subtitle_clip(subtitle, duration)
        subtitle_bg = ColorClip(size=(self.video_size[0], 150), color=(0, 0, 0)) \
            .with_opacity(0.6).with_duration(duration) \
            .with_position((0, self.video_size[1] - 150))

        return CompositeVideoClip([oc.with_position(('center', 'center')), subtitle_bg, subtitle_clip],
                                  size=self.video_size).with_duration(duration)

    def _create_main_clip(self, scene, subtitle, duration):
        """메인/인트로 장면 생성"""
        # 이미지 경로 확인
        image_path = os.path.join(self.image_dir, f"{scene['scene_id']}.jpeg")
        if not os.path.exists(image_path):
            image_path = os.path.join(self.image_dir, f"{scene['scene_id']}.jpg")


        # 이미지 클립 생성
        img_clip = ImageClip(image_path, duration=duration)
        img_w, img_h = img_clip.size

        # 원본 비율 유지하면서 화면에 맞게 크기 조정
        scale = min(self.video_size[0] / img_w, self.video_size[1] / img_h)
        resized_img = img_clip.resized(scale)

        # 중앙 배치 (검은 배경은 자동으로 생성됨)
        positioned_img = resized_img.with_position(('center', 'center'))

        # 자막 클립 생성
        subtitle_clip = self.create_subtitle_clip(subtitle, duration)

        # 자막 배경 생성
        subtitle_bg = ColorClip(
            size=(self.video_size[0], 150),
            color=(0, 0, 0)
        ).with_opacity(0.6).with_duration(duration).with_position((0, self.video_size[1] - 150))

        # 최종 장면 클립 합성
        scene_clip = CompositeVideoClip([
            positioned_img,
            subtitle_bg,
            subtitle_clip
        ], size=self.video_size).with_duration(duration)

        return scene_clip

    def _load_background_music(self, total_duration):
        """배경 음악 로드"""
        music_path = os.path.join(self.music_dir, "memorial_music.mp3")
        audio = AudioFileClip(music_path)

        if audio.duration + 0.05 < total_duration:
            audio =  audio.with_effects([afx.AudioLoop(duration=total_duration)])

        return audio


    def create_video(self, keywords, name, birth, death, photo_count,upload_to_blob=True):
        """메인 비디오 생성 함수"""
        print("📝 시나리오 생성 중...")
        if TEST_MODE:
            scenes = [
                      {'scene_id': 1, 'scene_type': 'intro', 'subtitle': '송해 (1927.04.27 - 2022.06.08)\n국민의 MC, 영원한 목소리.', 'duration': 6},
                      {'scene_id': 2, 'scene_type': 'main', 'subtitle': '언제나 유쾌한 웃음으로\n국민들에게 희망을 주셨습니다.', 'duration': 7},
                      {'scene_id': 3, 'scene_type': 'main', 'subtitle': '전국 방방곡곡을 누비며\n서민과 함께 웃고 울던 길.', 'duration': 7},
                      {'scene_id': 4, 'scene_type': 'main', 'subtitle': '무대 위에서 들려주신 목소리는\n세대를 아우르는 따뜻한 노래였습니다.', 'duration': 6},
                      {'scene_id': 5, 'scene_type': 'main', 'subtitle': '누구보다도 친근하고 다정한\n국민의 MC로 기억합니다.', 'duration': 7},
                      {'scene_id': 6, 'scene_type': 'main', 'subtitle': '재치 넘치는 입담과\n따뜻한 시선이 그립습니다.', 'duration': 7},
                      {'scene_id': 7, 'scene_type': 'main', 'subtitle': '언제나 무대 아래 청중에게\n사랑과 용기를 주셨습니다.', 'duration': 6},
                      {'scene_id': 8, 'scene_type': 'main', 'subtitle': '그 웃음소리는\n우리 마음속에 오래 남아 있습니다.', 'duration': 7},
                      {'scene_id': 9, 'scene_type': 'main', 'subtitle': '한국 방송사의 산증인으로서\n문화의 한 시대를 빛내셨습니다.', 'duration': 6},
                      {'scene_id': 10, 'scene_type': 'main', 'subtitle': '노래와 함께한 순간들이\n아직도 눈앞에 선합니다.', 'duration': 7},
                      {'scene_id': 11, 'scene_type': 'main', 'subtitle': '서민과 함께 울고 웃던 모습,\n그 따뜻한 발걸음을 기억합니다.', 'duration': 7},
                      {'scene_id': 12, 'scene_type': 'main', 'subtitle': '전국노래자랑의 무대 위에서\n언제나 청춘과 함께하셨습니다.', 'duration': 6},
                      {'scene_id': 13, 'scene_type': 'main', 'subtitle': '당신이 남긴 유산은\n대한민국의 소중한 기억입니다.', 'duration': 7},
                      {'scene_id': 14, 'scene_type': 'main', 'subtitle': '우리 모두의 삶을 노래해 주신\n그 따뜻한 마음에 감사합니다.', 'duration': 6},
                      {'scene_id': 15, 'scene_type': 'main', 'subtitle': '영원히 국민과 함께한\nMC 송해 선생님을 추억합니다.', 'duration': 7},
                      {'scene_id': 16, 'scene_type': 'outro', 'subtitle': '국민의 MC, 송해 선생님.\n사랑과 존경을 담아 영원히 기억하겠습니다.', 'duration': 7}
                    ]

        else:
            scenes = generate_scenario(keywords, name, birth, death, photo_count+1)

        print(f"시나리오: {len(scenes)}개 장면")
        for scene in scenes:
            print(f"  - {scene['scene_type']}: {scene['subtitle'][:20]}... ({scene['duration']}초)")
        print("-" * 50)


        print("🎬 영상 클립 생성 중...")
        clips = []
        total_duration = sum(s['duration'] for s in scenes)

        # 배경 음악 로드
        audio = self._load_background_music(total_duration)
        if audio is None:
            print("배경음악이 로드 되지 않았습니다.")

        # 각 장면별로 클립 생성
        for i, scene in enumerate(scenes):
            print(f"  장면 {i + 1}/{len(scenes)} 생성 중...")
            scene_clip = self.create_scene_clip(scene)
            clips.append(scene_clip)

        print("🎭 장면 전환 효과 적용 중...")
        # 크로스페이드 효과와 함께 클립 연결
        transition_duration = 0
        fading_clips = []
        current_time = 0

        for i, clip in enumerate(clips):
            if i > 0:
                current_time -= transition_duration

            fading_clips.append(clip.with_start(current_time))
            current_time += clip.duration

        # 최종 비디오 합성
        final_clip = CompositeVideoClip(fading_clips, size=self.video_size)
        final_clip.duration = current_time - transition_duration
        final_clip = final_clip.with_audio(audio)

        # 비디오 출력
        output_file = os.path.join(self.output_dir, "memorial_video.mp4")
        print("🎥 최종 영상 렌더링 중...")

        final_clip.write_videofile(
            output_file,
            codec='libx264',
            audio_codec='aac',
            temp_audiofile='temp-audio.m4a',
            remove_temp=True,
            fps=24
        )

        print(f"✅ 영상 제작 완료! '{output_file}' 파일로 저장되었습니다.")

        # Blob Storage 업로드
        if upload_to_blob:
            print("☁️  Azure Blob Storage 업로드 중...")
            blob_url = upload_video_to_blob(output_file, memorialId=self.memorialId, videoId=self.video_id)

            return blob_url
        else:
            return {
                'local_file': output_file,
                'blob_result': None
            }

if __name__ == "__main__":
    base_path = os.path.join("../temp", "test")
    keywords = "그리움, 아버지, 든든한 아버지"
    name = "송해"
    birth = "1975-03-02"
    death = "2023-12-27"
    photo_count = 15
    creator = MemorialVideoCreator(base_path)
    output_file = creator.create_video(keywords, name, birth, death, photo_count + 1, upload_to_blob=False)

    if output_file:
        print(f"🎉 모든 작업이 완료되었습니다!")
        print(f"📁 결과 파일: {output_file}")