$version = '4.0.6'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'F67C45F5A95357D23255EB9F7818CF499A0AD9B15CC42B3B7833582DA897358C'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
