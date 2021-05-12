$version = '2.5.1'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '216BA25AC5FCF0EF573D830FDCB7DDD34010D886D1F30E7D600DFF45226CC8B4'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
