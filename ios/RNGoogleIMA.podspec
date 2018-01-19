
Pod::Spec.new do |s|
  s.name         = "RNGoogleIMA"
  s.version      = "1.0.0"
  s.summary      = "RNGoogleIMA"
  s.description  = <<-DESC
                  RNGoogleIMA
                   DESC
  s.homepage     = ""
  s.license      = "MIT"
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = { "author" => "author@domain.cn" }
  s.platform     = :ios, "7.0"
  s.source       = { :git => "https://github.com/Magiczvn/react-native-google-ima.git", :tag => "master" }
  s.source_files  = "RNGoogleIMA/**/*.{h,m}"
  s.requires_arc = true


  s.dependency "React"
  #s.dependency "others"

end

  