/*
 * Léon - The URL Cleaner
 * Copyright (C) 2022 Sven Jacobs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.svenjacobs.app.leon.feature.sanitizer.amazon

import com.svenjacobs.app.leon.feature.sanitizer.youtube.YoutubeShortUrlSanitizer
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe

class YoutubeShortUrlSanitizerTest : WordSpec(
	{

		"invoke" should {

			"convert youtu.be short URL into long youtube.com URL" {
				val sanitizer = YoutubeShortUrlSanitizer()
				val result = sanitizer("https://youtu.be/5HaUOgW5BlA")
				result shouldBe "https://www.youtube.com/watch?v=5HaUOgW5BlA"
			}
		}
	},
)