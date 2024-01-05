$version = '4.2.3'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'AAB1EB197F3D249B6629A202F2E9C8B3F43EF8E3C91459669FDF3B189875719A'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
